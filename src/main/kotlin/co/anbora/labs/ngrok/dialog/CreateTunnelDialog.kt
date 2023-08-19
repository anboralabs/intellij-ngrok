package co.anbora.labs.ngrok.dialog

import co.anbora.labs.ngrok.license.CheckLicense
import com.github.alexdlaird.ngrok.protocol.Proto
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.not
import com.intellij.ui.layout.selected
import javax.swing.Action
import javax.swing.ComboBoxModel
import javax.swing.JButton
import javax.swing.JComponent

class CreateTunnelDialog: DialogWrapper(true) {

    private lateinit var protocolField: Cell<ComboBox<Proto>>
    private lateinit var portField: Cell<JBTextField>

    private lateinit var hostField: Cell<JBTextField>
    private lateinit var subdomainField: Cell<JBTextField>

    private val hostCheckBox: JBCheckBox = JBCheckBox("Host:")
    private val subdomainCheckBox: JBCheckBox = JBCheckBox("Subdomain:")

    private val MESSAGE = "The port number should be between 0 and 65535"

    private val DEFAULT_HOST = "http://localhost"
    private val DEFAULT_PORT = 8080

    val licensed = CheckLicense.isLicensed() ?: false

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
            collapsibleGroup("Advanced Paid Options", false) {
                twoColumnsRow({
                    cell(hostCheckBox)
                        .enabledIf(subdomainCheckBox.selected.not())
                }, {
                    val textField = JBTextField()
                    textField.toolTipText = DEFAULT_HOST
                    hostField = cell(textField)
                        .columns(COLUMNS_MEDIUM)
                        .enabledIf(hostCheckBox.selected)
                })
                twoColumnsRow({
                    cell(subdomainCheckBox)
                        .enabledIf(hostCheckBox.selected.not())
                }, {
                    subdomainField = textField()
                        .columns(COLUMNS_MEDIUM)
                        .enabledIf(subdomainCheckBox.selected)
                })
            }.expanded = false
        }
    }

    override fun getHelpId(): String? {
        if (licensed) return null
        return "co.anbora.labs.ngrok.help"
    }

    override fun setHelpTooltip(helpButton: JButton) {
        helpButton.toolTipText = "Buy license"
    }

    override fun doHelpAction() {
        CheckLicense.requestLicense("Support my work buying the license 1 USD")
    }

    override fun doValidate(): ValidationInfo? {
        if (licensed) {
            return null
        }
        if (hostCheckBox.isSelected) {
            return ValidationInfo("Paid options, support my work buying the license 1 USD")
                .forComponent(hostCheckBox)
        }
        if (subdomainCheckBox.isSelected) {
            return ValidationInfo("Paid options, support my work buying the license 1 USD")
                .forComponent(subdomainCheckBox)
        }
        return null
    }

    fun protocol(): Proto = protocolField.component.item

    fun port(): Int = portField.component.text.toInt()

    fun host(): String? = if (hostCheckBox.isSelected) hostField.component.text else null

    fun subdomain(): String? = if (subdomainCheckBox.isSelected) subdomainField.component.text else null

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
