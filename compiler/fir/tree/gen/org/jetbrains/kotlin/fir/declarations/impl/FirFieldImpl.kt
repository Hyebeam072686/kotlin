/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.declarations.impl

import org.jetbrains.kotlin.fir.FirImplementationDetail
import org.jetbrains.kotlin.fir.FirModuleData
import org.jetbrains.kotlin.fir.FirSourceElement
import org.jetbrains.kotlin.fir.declarations.DeprecationsPerUseSite
import org.jetbrains.kotlin.fir.declarations.FirDeclarationAttributes
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.FirDeclarationStatus
import org.jetbrains.kotlin.fir.declarations.FirField
import org.jetbrains.kotlin.fir.declarations.FirPropertyAccessor
import org.jetbrains.kotlin.fir.declarations.FirPropertyFieldDeclaration
import org.jetbrains.kotlin.fir.declarations.FirResolvePhase
import org.jetbrains.kotlin.fir.declarations.FirTypeParameterRef
import org.jetbrains.kotlin.fir.expressions.FirAnnotationCall
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.symbols.impl.FirFieldSymbol
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.serialization.deserialization.descriptors.DeserializedContainerSource
import org.jetbrains.kotlin.fir.visitors.*

/*
 * This file was generated automatically
 * DO NOT MODIFY IT MANUALLY
 */

class FirFieldImpl @FirImplementationDetail constructor(
    override val source: FirSourceElement?,
    override val moduleData: FirModuleData,
    @Volatile
    override var resolvePhase: FirResolvePhase,
    override val origin: FirDeclarationOrigin,
    override val attributes: FirDeclarationAttributes,
    override var returnTypeRef: FirTypeRef,
    override val typeParameters: MutableList<FirTypeParameterRef>,
    override var status: FirDeclarationStatus,
    override var deprecation: DeprecationsPerUseSite?,
    override val containerSource: DeserializedContainerSource?,
    override val dispatchReceiverType: ConeKotlinType?,
    override val name: Name,
    override var initializer: FirExpression?,
    override val isVar: Boolean,
    override var backingField: FirPropertyFieldDeclaration?,
    override val annotations: MutableList<FirAnnotationCall>,
    override val symbol: FirFieldSymbol,
) : FirField() {
    override val receiverTypeRef: FirTypeRef? get() = null
    override val delegate: FirExpression? get() = null
    override val isVal: Boolean get() = !isVar
    override val getter: FirPropertyAccessor? get() = null
    override val setter: FirPropertyAccessor? get() = null

    init {
        symbol.bind(this)
    }

    override fun <R, D> acceptChildren(visitor: FirVisitor<R, D>, data: D) {
        returnTypeRef.accept(visitor, data)
        typeParameters.forEach { it.accept(visitor, data) }
        status.accept(visitor, data)
        initializer?.accept(visitor, data)
        backingField?.accept(visitor, data)
        annotations.forEach { it.accept(visitor, data) }
    }

    override fun <D> transformChildren(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        transformReturnTypeRef(transformer, data)
        transformTypeParameters(transformer, data)
        transformStatus(transformer, data)
        transformInitializer(transformer, data)
        transformBackingField(transformer, data)
        transformOtherChildren(transformer, data)
        return this
    }

    override fun <D> transformReturnTypeRef(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        returnTypeRef = returnTypeRef.transform(transformer, data)
        return this
    }

    override fun <D> transformTypeParameters(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        typeParameters.transformInplace(transformer, data)
        return this
    }

    override fun <D> transformStatus(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        status = status.transform(transformer, data)
        return this
    }

    override fun <D> transformReceiverTypeRef(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        return this
    }

    override fun <D> transformInitializer(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        initializer = initializer?.transform(transformer, data)
        return this
    }

    override fun <D> transformDelegate(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        return this
    }

    override fun <D> transformGetter(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        return this
    }

    override fun <D> transformSetter(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        return this
    }

    override fun <D> transformBackingField(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        backingField = backingField?.transform(transformer, data)
        return this
    }

    override fun <D> transformAnnotations(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        annotations.transformInplace(transformer, data)
        return this
    }

    override fun <D> transformOtherChildren(transformer: FirTransformer<D>, data: D): FirFieldImpl {
        transformAnnotations(transformer, data)
        return this
    }

    override fun replaceResolvePhase(newResolvePhase: FirResolvePhase) {
        resolvePhase = newResolvePhase
    }

    override fun replaceReturnTypeRef(newReturnTypeRef: FirTypeRef) {
        returnTypeRef = newReturnTypeRef
    }

    override fun replaceReceiverTypeRef(newReceiverTypeRef: FirTypeRef?) {}

    override fun replaceDeprecation(newDeprecation: DeprecationsPerUseSite?) {
        deprecation = newDeprecation
    }

    override fun replaceInitializer(newInitializer: FirExpression?) {
        initializer = newInitializer
    }

    override fun replaceGetter(newGetter: FirPropertyAccessor?) {}

    override fun replaceSetter(newSetter: FirPropertyAccessor?) {}
}
