package co.anbora.labs.ngrok.dialog

import com.github.alexdlaird.ngrok.protocol.Proto
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.ValidationInfoBuilder
import javax.swing.ComboBoxModel
import javax.swing.JComponent

class CreateTunnelDialog: DialogWrapper(true) {

    private lateinit var protocolField: Cell<ComboBox<Proto>>
    private lateinit var portField: Cell<JBTextField>

    private val MESSAGE = "The port number should be between 0 and 65535"

    private val DEFAULT_PORT = 8080

    init {
        title = "Create Tunnel"
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row("Protocol:") {
                protocolField = comboBox(protocols())
                    .columns(COLUMNS_MEDIUM)
            }
            row("Port:") {
                portField = textField()
                    .text(DEFAULT_PORT.toString())
                    .validationOnInput(validatePort())
                    .columns(COLUMNS_MEDIUM)
            }
        }
    }

    fun protocol(): Proto = protocolField.component.item

    fun port(): Int = portField.component.text.toInt()

    private fun validatePort(): ValidationInfoBuilder.(JBTextField) -> ValidationInfo? = {
            val pt: String = it.text
            if (StringUtil.isNotEmpty(pt)) {
                try {
                    val portValue = pt.toInt()
                    if (portValue in 0..65535) {
                        null
                    } else {
                        this.error(MESSAGE)
                    }
                } catch (nfe: NumberFormatException) {
                    this.error(MESSAGE)
                }
            } else {
                this.error(MESSAGE)
            }
        }

    private fun protocols(): ComboBoxModel<Proto> {
        return CollectionComboBoxModel(Proto.values().toList())
    }
}