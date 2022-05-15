package co.anbora.labs.ngrok.remote.server.deployment

import co.anbora.labs.ngrok.remote.server.NgrokHostConfiguration
import com.intellij.openapi.components.service
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.configuration.deployment.DeploymentConfigurator
import com.intellij.remoteServer.configuration.deployment.DeploymentSource

private const val DEFAULT_CONFIGURATION_NAME = "Ngrok Run"

class NgrokDeploymentConfigurator(
    private val project: Project
): DeploymentConfigurator<NgrokDeploymentConfiguration, NgrokHostConfiguration>() {

    override fun getAvailableDeploymentSources(): MutableList<DeploymentSource> =
        emptyList<DeploymentSource>().toMutableList()

    override fun createDefaultConfiguration(
        source: DeploymentSource
    ): NgrokDeploymentConfiguration = NgrokDeploymentConfiguration()

    override fun createEditor(
        source: DeploymentSource,
        server: RemoteServer<NgrokHostConfiguration>?
    ): SettingsEditor<NgrokDeploymentConfiguration> = NgrokDeploymentEditor(service())

    override fun suggestConfigurationName(
        deploymentSource: DeploymentSource,
        deploymentConfiguration: NgrokDeploymentConfiguration
    ): String = DEFAULT_CONFIGURATION_NAME

    override fun isGeneratedConfigurationName(
        name: String,
        deploymentSource: DeploymentSource,
        deploymentConfiguration: NgrokDeploymentConfiguration
    ): Boolean = name == DEFAULT_CONFIGURATION_NAME

}