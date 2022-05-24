package co.anbora.labs.ngrok.ui.builder.impl

import co.anbora.labs.ngrok.ui.builder.MutableProperty

data class MutablePropertyImpl<T>(val getter: () -> T, val setter: (value: T) -> Unit) : MutableProperty<T> {

    override fun get(): T {
        return getter()
    }

    override fun set(value: T) {
        setter(value)
    }
}
