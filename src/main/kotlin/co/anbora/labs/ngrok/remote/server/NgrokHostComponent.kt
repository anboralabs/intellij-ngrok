package co.anbora.labs.ngrok.remote.server

import co.anbora.labs.ngrok.toolchain.NgrokToolchainService
import com.github.alexdlaird.ngrok.protocol.Region
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import javax.swing.ComboBoxModel
import javax.swing.JPanel

class NgrokHostComponent {
    private val panel: JPanel
    private lateinit var hostField: Cell<ComboBox<String>>
    private lateinit var apiTokenField: Cell<JBTextField>
    private lateinit var regionsField: Cell<ComboBox<Region>>
    private lateinit var regionCheckBox: Cell<JBCheckBox>

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
            twoColumnsRow({
                regionCheckBox = checkBox("Region:")
            }, {
                regionsField = comboBox(regions())
                    .columns(COLUMNS_MEDIUM)
                    .enabledIf(regionCheckBox.selected)
            })
        }
    }

    fun getPanel(): JPanel = panel

    private fun ngrokHosts(): ComboBoxModel<String> {
        return CollectionComboBoxModel(listOf(NgrokToolchainService.toolchainSettings.toolchain().binPath()))
    }

    private fun regions(): ComboBoxModel<Region> {
        return CollectionComboBoxModel(listOf(*Region.values()))
    }

    fun getApiToken(): String = apiTokenField.component.text

    fun setApiToken(token: String) {
        apiTokenField.component.text = token
    }

    private fun isRegionSelected(): Boolean = regionCheckBox.component.isSelected
    fun getRegion(): Region? = if (isRegionSelected()) regionsField.component.item else null

    fun setRegion(region: Region?) {
        regionsField.component.item = region
    }
}
