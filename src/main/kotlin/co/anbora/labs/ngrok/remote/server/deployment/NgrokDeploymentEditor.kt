package co.anbora.labs.ngrok.remote.server.deployment

import com.intellij.execution.ui.FragmentedSettingsEditor
import com.intellij.execution.ui.SettingsEditorFragment
import com.intellij.execution.ui.utils.FragmentsBuilder

class NgrokDeploymentEditor: FragmentedSettingsEditor<NgrokDeploymentConfiguration>(null) {

    override fun createFragments(): MutableCollection<SettingsEditorFragment<NgrokDeploymentConfiguration, *>> {
        return FragmentsBuilder<NgrokDeploymentConfiguration>(null, "ngrok", emptyList())
            .build()
    }

}