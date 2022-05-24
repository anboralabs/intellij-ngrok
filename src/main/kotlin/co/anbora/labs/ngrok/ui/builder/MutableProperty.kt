package co.anbora.labs.ngrok.ui.builder

import com.intellij.ui.dsl.builder.MutableProperty
import com.intellij.ui.dsl.builder.impl.MutablePropertyImpl

interface MutableProperty<T> {

    fun get(): T
    fun set(value: T)

}

@Suppress("FunctionName")
fun <T> MutableProperty(getter: () -> T, setter: (value: T) -> Unit): MutableProperty<T> {
    return MutablePropertyImpl(getter, setter)
}