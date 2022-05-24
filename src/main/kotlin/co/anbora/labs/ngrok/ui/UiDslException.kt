package co.anbora.labs.ngrok.ui

import co.anbora.labs.ngrok.ui.gridLayout.Constraints
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.diagnostic.logger
import java.awt.Component
import javax.swing.JComponent

class UiDslException(message: String = "Internal error", cause: Throwable? = null) : RuntimeException(message, cause) {

    companion object {
        fun error(message: String) {
            if (PluginManagerCore.isRunningFromSources()) {
                throw UiDslException(message)
            }
            else {
                logger<UiDslException>().error(message)
            }
        }
    }
}

fun checkTrue(value: Boolean) {
    if (!value) {
        throw UiDslException()
    }
}

fun checkPositive(name: String, value: Int) {
    if (value <= 0) {
        throw UiDslException("Value must be positive: $name = $value")
    }
}

fun checkNonNegative(name: String, value: Int) {
    if (value < 0) {
        throw UiDslException("Value cannot be negative: $name = $value")
    }
}

fun checkConstraints(constraints: Any?): Constraints {
    if (constraints !is Constraints) {
        throw UiDslException("Invalid constraints: $constraints")
    }

    return constraints
}

fun checkComponent(component: Component?): JComponent {
    if (component !is JComponent) {
        throw UiDslException("Only JComponents are supported: $component")
    }

    return component
}