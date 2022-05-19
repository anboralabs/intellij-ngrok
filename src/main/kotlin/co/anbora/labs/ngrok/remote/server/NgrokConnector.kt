package co.anbora.labs.ngrok.remote.server

import co.anbora.labs.ngrok.remote.server.deployment.NgrokDeploymentConfiguration
import co.anbora.labs.ngrok.service.NgrokApplicationManager
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor

class NgrokConnector(
    private val configuration: NgrokHostConfiguration,
    private val taskExecutor: ServerTaskExecutor,
    private val applicationManager: NgrokApplicationManager
): ServerConnector<NgrokDeploymentConfiguration>() {
    override fun connect(
        callback: ConnectionCallback<NgrokDeploymentConfiguration>
    ) {
        taskExecutor.submit( {
            NgrokServerRuntimeInstance(
                configuration,
                taskExecutor,
                applicationManager
            ).connect(callback)
        }, callback)
    }
}