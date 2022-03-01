/*
 * Copyright 2010-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

#pragma once

#include <cstddef>

#include "Allocator.hpp"
#include "GCScheduler.hpp"
#include "IntrusiveList.hpp"
#include "ObjectFactory.hpp"
#include "ScopedThread.hpp"
#include "Types.h"
#include "Utils.hpp"
#include "GCState.hpp"

namespace kotlin {

namespace mm {
class ThreadData;
}

namespace gc {

class FinalizerProcessor;

// Stop-the-world mark + concurrent sweep. The GC runs in a separate thread, finalizers run in another thread of their own.
// TODO: Also make mark concurrent.
class ConcurrentMarkAndSweep : private Pinned {
public:
    class ObjectData {
    public:
        enum class Color {
            kWhite = 0, // Initial color at the start of collection cycles. Objects with this color at the end of GC cycle are collected.
                        // All new objects are allocated with this color.
            kBlack, // Objects encountered during mark phase.
        };

        Color color() const noexcept { return color_; }
        void setColor(Color color) noexcept { color_ = color; }

        void* next() const noexcept { return next_; }
        void setNext(void* next) noexcept { next_ = next; }

    private:
        // HeapObjHeader* in reality.
        // TODO: I think we should move object headers from `ObjectFactory` to each GC implementation,
        //       and use traits to provide access to fields (like in intrusive list).
        void* next_ = nullptr;
        Color color_ = Color::kWhite;
    };

    class ThreadData : private Pinned {
    public:
        using ObjectData = ConcurrentMarkAndSweep::ObjectData;
        using Allocator = AllocatorWithGC<AlignedAllocator, ThreadData>;

        explicit ThreadData(ConcurrentMarkAndSweep& gc, mm::ThreadData& threadData, GCSchedulerThreadData& gcScheduler) noexcept :
            gc_(gc), gcScheduler_(gcScheduler) {}
        ~ThreadData() = default;

        void SafePointAllocation(size_t size) noexcept;

        void ScheduleAndWaitFullGC() noexcept;
        void ScheduleAndWaitFullGCWithFinalizers() noexcept;

        void OnOOM(size_t size) noexcept;

        Allocator CreateAllocator() noexcept { return Allocator(AlignedAllocator(), *this); }

    private:
        ConcurrentMarkAndSweep& gc_;
        GCSchedulerThreadData& gcScheduler_;
    };

    using Allocator = ThreadData::Allocator;

    using HeapObjHeader = mm::ObjectFactory<ConcurrentMarkAndSweep>::HeapObjHeader;

    struct ObjectTraits {
        static HeapObjHeader* next(const HeapObjHeader& value) noexcept {
            return static_cast<HeapObjHeader*>(value.gcData.next());
        }

        static void setNext(HeapObjHeader& value, HeapObjHeader* next) noexcept {
            value.gcData.setNext(next);
        }
    };

    using MarkQueue = intrusive_forward_list<HeapObjHeader, ObjectTraits>;

    ConcurrentMarkAndSweep(mm::ObjectFactory<ConcurrentMarkAndSweep>& objectFactory, GCScheduler& scheduler) noexcept;
    ~ConcurrentMarkAndSweep();

    void StartFinalizerThreadIfNeeded() noexcept;
    void StopFinalizerThreadIfRunning() noexcept;
    bool FinalizersThreadIsRunning() noexcept;

private:
    // Returns `true` if GC has happened, and `false` if not (because someone else has suspended the threads).
    bool PerformFullGC(int64_t epoch) noexcept;

    mm::ObjectFactory<ConcurrentMarkAndSweep>& objectFactory_;
    GCScheduler& gcScheduler_;

    uint64_t lastGCTimestampUs_ = 0;
    GCStateHolder state_;
    ScopedThread gcThread_;
    KStdUniquePtr<FinalizerProcessor> finalizerProcessor_;

    MarkQueue markQueue_;
};

} // namespace gc
} // namespace kotlin
