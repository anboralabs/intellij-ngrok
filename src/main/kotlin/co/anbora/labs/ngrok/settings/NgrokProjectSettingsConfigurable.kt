package co.anbora.labs.ngrok.settings

import co.anbora.labs.ngrok.toolchain.NgrokToolchain
import co.anbora.labs.ngrok.toolchain.NgrokToolchainService.Companion.toolchainSettings
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import javax.swing.JComponent

class NgrokProjectSettingsConfigurable(private val project: Project) : Configurable, Disposable {

    private val mainPanel: DialogPanel
    private val model = NgrokProjectSettingsForm.Model(
        homeLocation = "",
    )
    private val settingsForm = NgrokProjectSettingsForm(project, model)

    init {
        mainPanel = settingsForm.createComponent()

        mainPanel.registerValidators(this)
    }

    override fun createComponent(): JComponent = mainPanel

    override fun getPreferredFocusedComponent(): JComponent = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()

        val settings = toolchainSettings
        return model.homeLocation != settings.toolchainLocation
    }

    override fun apply() {
        mainPanel.apply()

        validateSettings()

        val settings = toolchainSettings
        settings.setToolchain(NgrokToolchain.fromPath(model.homeLocation))
    }

    private fun validateSettings() {
        val issues = mainPanel.validateAll()
        if (issues.isNotEmpty()) {
            throw ConfigurationException(issues.first().message)
        }
    }

    override fun reset() {
        val settings = toolchainSettings

        with(model) {
            homeLocation = settings.toolchainLocation
        }

        settingsForm.reset()
        mainPanel.reset()
    }

    override fun getDisplayName(): String = "Ngrok"

    companion object {
        @JvmStatic
        fun show(project: Project) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, NgrokProjectSettingsConfigurable::class.java)
        }
    }

    override fun dispose() {
    }
}
