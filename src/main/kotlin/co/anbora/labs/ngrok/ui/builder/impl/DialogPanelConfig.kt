package co.anbora.labs.ngrok.ui.builder.impl

import com.intellij.openapi.ui.validation.DialogValidation
import com.intellij.openapi.ui.validation.DialogValidationRequestor
import com.intellij.util.SmartList
import javax.swing.JComponent

class DialogPanelConfig {

    val context = Context()

    var preferredFocusedComponent: JComponent? = null

    val applyCallbacks = linkedMapOf<JComponent?, MutableList<() -> Unit>>()
    val resetCallbacks = linkedMapOf<JComponent?, MutableList<() -> Unit>>()
    val isModifiedCallbacks = linkedMapOf<JComponent?, MutableList<() -> Boolean>>()

    val validationRequestors = linkedMapOf<JComponent, MutableList<DialogValidationRequestor>>()
    val validationsOnInput = linkedMapOf<JComponent, MutableList<DialogValidation>>()
    val validationsOnApply = linkedMapOf<JComponent, MutableList<DialogValidation>>()
}

fun <T> MutableMap<JComponent?, MutableList<() -> T>>.register(component: JComponent?, callback: () -> T) {
    getOrPut(component) { SmartList() }.add(callback)
}