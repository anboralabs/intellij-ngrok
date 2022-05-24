package co.anbora.labs.ngrok.ui

import co.anbora.labs.ngrok.ui.builder.IntelliJSpacingConfiguration
import co.anbora.labs.ngrok.ui.builder.Panel
import co.anbora.labs.ngrok.ui.builder.impl.DialogPanelConfig
import co.anbora.labs.ngrok.ui.builder.impl.PanelBuilder
import co.anbora.labs.ngrok.ui.builder.impl.PanelImpl
import co.anbora.labs.ngrok.ui.gridLayout.GridLayout
import com.intellij.openapi.ui.DialogPanel

@DslMarker
internal annotation class LayoutDslMarker

/**
 * Root panel that provided by [init] does not support [CellBase] methods now. May be added later but seems not needed now
 */
fun panel(init: Panel.() -> Unit): DialogPanel {
    val dialogPanelConfig = DialogPanelConfig()
    val panel = PanelImpl(dialogPanelConfig, IntelliJSpacingConfiguration(), null)
    panel.init()
    dialogPanelConfig.context.postInit()

    val layout = GridLayout()
    val result = DialogPanel(layout = layout)
    val builder = PanelBuilder(panel.rows, dialogPanelConfig, panel.spacingConfiguration, result, layout.rootGrid)
    builder.build()
    initPanel(dialogPanelConfig, result)
    return result
}

private fun initPanel(dialogPanelConfig: DialogPanelConfig, panel: DialogPanel) {
    panel.preferredFocusedComponent = dialogPanelConfig.preferredFocusedComponent

    panel.applyCallbacks = dialogPanelConfig.applyCallbacks
    panel.resetCallbacks = dialogPanelConfig.resetCallbacks
    panel.isModifiedCallbacks = dialogPanelConfig.isModifiedCallbacks

    panel.validationRequestors = dialogPanelConfig.validationRequestors
    panel.validationsOnInput = dialogPanelConfig.validationsOnInput
    panel.validationsOnApply = dialogPanelConfig.validationsOnApply
}