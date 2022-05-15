package co.anbora.labs.ngrok.remote.server

import co.anbora.labs.ngrok.icons.NgrokIcons
import co.anbora.labs.ngrok.remote.server.deployment.NgrokDeploymentConfigurator
import co.anbora.labs.ngrok.remote.server.deployment.NgrokSingletonDeploymentSourceType
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.RemoteServerConfigurable
import com.intellij.remoteServer.ServerType
import com.intellij.remoteServer.configuration.deployment.DeploymentConfigurator
import com.intellij.remoteServer.configuration.deployment.SingletonDeploymentSourceType
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import javax.swing.Icon

class NgrokHostType: ServerType<NgrokHostConfiguration>("ngrok") {

    companion object {
        fun getInstance(): NgrokHostType {
            return EP_NAME.findExtension(NgrokHostType::class.java)!!
        }
    }

    override fun getPresentableName(): String = "Ngrok"

    override fun getDeploymentConfigurationTypePresentableName(): String = "Ngrok"

    override fun getDeploymentConfigurationFactoryId(): String = "Ngrok"

    override fun getIcon(): Icon = NgrokIcons.NGROK

    override fun createDefaultConfiguration(): NgrokHostConfiguration = NgrokHostConfiguration()

    override fun createServerConfigurable(
        configuration: NgrokHostConfiguration
    ): RemoteServerConfigurable = NgrokHostConfigurable()

    override fun createDeploymentConfigurator(
        project: Project
    ): DeploymentConfigurator<*, NgrokHostConfiguration> = NgrokDeploymentConfigurator(project)

    override fun createConnector(
        configuration: NgrokHostConfiguration,
        asyncTasksExecutor: ServerTaskExecutor
    ): ServerConnector<*> = NgrokConnector(configuration, asyncTasksExecutor)

    override fun getCustomToolWindowId(): String = "Services"

    override fun getSingletonDeploymentSourceTypes(): MutableList<SingletonDeploymentSourceType> =
        listOf(NgrokSingletonDeploymentSourceType.getInstance()).toMutableList()

    override fun mayHaveProjectSpecificDeploymentSources(): Boolean = false
}