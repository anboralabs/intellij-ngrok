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
    private lateinit var tyeHostField: Cell<ComboBox<String>>

    init {
        panel = panel {
            row("Ngrok host:") {
                tyeHostField = comboBox(ngrokHosts())
                    .columns(COLUMNS_MEDIUM)
            }
        }
    }

    fun getPanel(): JPanel = panel

    private fun ngrokHosts(): ComboBoxModel<String> {
        return CollectionComboBoxModel(listOf("Embedded"))
    }
}