// !DIAGNOSTICS: -UNUSED_VARIABLE
// !WITH_NEW_INFERENCE

open class View

fun test() {
    val target = foo<View>() ?: foo() <!NI;USELESS_ELVIS!>?: run {}<!>
}

fun <T : View> foo(): T? {
    return null
}
