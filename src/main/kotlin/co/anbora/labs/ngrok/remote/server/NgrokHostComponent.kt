package co.anbora.labs.ngrok.remote.server

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.COLUMNS_MEDIUM
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import javax.swing.ComboBoxModel
import javax.swing.JPanel

class NgrokHostComponent {
    private val panel: JPanel
    private lateinit var hostField: Cell<ComboBox<String>>
    private lateinit var apiTokenField: Cell<JBTextField>

    init {
        panel = panel {
            row("Ngrok host:") {
                hostField = comboBox(ngrokHosts())
                    .columns(COLUMNS_MEDIUM)
            }
            row("API Token:") {
                apiTokenField = textField()
                    .columns(COLUMNS_MEDIUM)
            }
        }
    }

    fun getPanel(): JPanel = panel

    private fun ngrokHosts(): ComboBoxModel<String> {
        return CollectionComboBoxModel(listOf("Embedded"))
    }

    fun getApiToken(): String = apiTokenField.component.text

    fun setApiToken(token: String) {
        apiTokenField.component.text = token
    }
}