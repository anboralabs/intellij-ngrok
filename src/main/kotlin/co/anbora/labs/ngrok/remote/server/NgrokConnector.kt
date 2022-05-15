package co.anbora.labs.ngrok.remote.server

import co.anbora.labs.ngrok.remote.server.deployment.NgrokDeploymentConfiguration
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor

class NgrokConnector(
    private val configuration: NgrokHostConfiguration,
    private val taskExecutor: ServerTaskExecutor
): ServerConnector<NgrokDeploymentConfiguration>() {
    override fun connect(
        callback: ConnectionCallback<NgrokDeploymentConfiguration>
    ) {
        TODO("Not yet implemented")
    }
}