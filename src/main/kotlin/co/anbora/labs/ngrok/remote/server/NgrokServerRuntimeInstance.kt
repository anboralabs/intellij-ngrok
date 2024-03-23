package co.anbora.labs.ngrok.remote.server

import co.anbora.labs.ngrok.remote.server.deployment.NgrokDeploymentConfiguration
import co.anbora.labs.ngrok.service.NgrokApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.remoteServer.configuration.deployment.DeploymentSource
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentLogManager
import com.intellij.remoteServer.runtime.deployment.DeploymentRuntime
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance

class NgrokServerRuntimeInstance(
    private val configuration: NgrokHostConfiguration,
    private val taskExecutor: ServerTaskExecutor,
    private val ngrokApplicationManager: NgrokApplicationManager
): ServerRuntimeInstance<NgrokDeploymentConfiguration>() {

    private val log = logger<NgrokServerRuntimeInstance>()

    fun connect(callback: ServerConnector.ConnectionCallback<NgrokDeploymentConfiguration>) {
        log.info("Server connected")
        callback.connected(this)
    }

    override fun deploy(
        task: DeploymentTask<NgrokDeploymentConfiguration>,
        logManager: DeploymentLogManager,
        callback: DeploymentOperationCallback
    ) {
        taskExecutor.submit({
            ngrokApplicationManager.runApplication(configuration).let {
                callback.started(it)

                it.waitForReadiness()
                callback.succeeded(it)
            }
        }, callback)
    }

    override fun computeDeployments(callback: ComputeDeploymentsCallback) {
        taskExecutor.submit({
            ngrokApplicationManager.refreshApplication(configuration.apiKey).forEach {
                val deployment = callback.addDeployment(it.applicationName, it, it.status, it.statusText)
                it.deploymentModel = deployment
            }
            callback.succeeded()
        }, callback)
    }

    override fun disconnect() {
        log.info("Server disconnected")
    }

    override fun getDeploymentName(source: DeploymentSource, configuration: NgrokDeploymentConfiguration): String =
        "Ngrok Application"

    override fun getRuntimeDeploymentName(
        deploymentRuntime: DeploymentRuntime,
        source: DeploymentSource,
        configuration: NgrokDeploymentConfiguration
    ): String = "Ngrok Application"
}
