package co.anbora.labs.ngrok.remote.server.deployment

import co.anbora.labs.ngrok.settings.NgrokSettings
import com.intellij.execution.ui.FragmentedSettingsEditor
import com.intellij.execution.ui.SettingsEditorFragment
import com.intellij.execution.ui.utils.Fragment
import com.intellij.execution.ui.utils.FragmentsBuilder
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.components.JBTextField

class NgrokDeploymentEditor(
    private val ngrokSettings: NgrokSettings
): FragmentedSettingsEditor<NgrokDeploymentConfiguration>(null) {

    private val apiTokenField: LabeledComponent<JBTextField>

    init {
        val apiTextField = JBTextField()
        apiTokenField = LabeledComponent.create(apiTextField, "Token:")
        apiTokenField.labelLocation = "West"
    }

    override fun createFragments(): MutableCollection<SettingsEditorFragment<NgrokDeploymentConfiguration, *>> {
        val builder = FragmentsBuilder<NgrokDeploymentConfiguration>(null, "tye", emptyList())
        builder.fragment("ngrok.configuration.api", apiTokenField) { this.setupApiTokenField() }

        return builder.build()
    }

    private fun Fragment<NgrokDeploymentConfiguration, LabeledComponent<JBTextField>>.setupApiTokenField() {
        this.name = "Options"
        this.actionHint = "Execute ngrok with a different apiKey"
        this.isRemovable = false
        this.apply = { settings, component ->
            settings.apiKey = component.component.text
        }
        this.reset = { settings, component ->
            component.component.text = settings.apiKey ?: ngrokSettings.apiToken
        }
    }

}