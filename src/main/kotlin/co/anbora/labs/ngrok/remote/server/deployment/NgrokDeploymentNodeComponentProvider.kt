package co.anbora.labs.ngrok.remote.server.deployment

import co.anbora.labs.ngrok.remote.server.components.ApplicationDeploymentNodeComponent
import co.anbora.labs.ngrok.remote.server.components.EmptyDeploymentNodeComponent
import co.anbora.labs.ngrok.remote.server.components.NgrokDeploymentNodeComponent
import co.anbora.labs.ngrok.remote.server.components.TunnelDeploymentNodeComponent
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import co.anbora.labs.ngrok.runtimes.NgrokTunnelRuntime
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.runtime.ConnectionStatus
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection
import com.intellij.remoteServer.runtime.ServerConnectionListener

class NgrokDeploymentNodeComponentProvider(
    private val project: Project
) {

    private val map: MutableMap<Deployment, NgrokDeploymentNodeComponent> = mutableMapOf()

    init {
        project.messageBus.connect().subscribe(ServerConnectionListener.TOPIC, object : ServerConnectionListener {
            override fun onConnectionCreated(connection: ServerConnection<*>) {
            }

            override fun onConnectionStatusChanged(connection: ServerConnection<*>) {
                if (connection.status == ConnectionStatus.DISCONNECTED) {
                    map.filterKeys { it.connection == connection }
                        .forEach { map.remove(it.key) }
                }
            }

            override fun onDeploymentsChanged(connection: ServerConnection<*>) {
            }
        })
    }

    fun getComponent(deployment: Deployment): NgrokDeploymentNodeComponent =
        map.computeIfAbsent(deployment, this::createComponent)

    private fun createComponent(deployment: Deployment): NgrokDeploymentNodeComponent {
        return when (val runtime = deployment.runtime) {
            is NgrokApplicationRuntime -> ApplicationDeploymentNodeComponent(project, runtime)
            is NgrokTunnelRuntime -> TunnelDeploymentNodeComponent(runtime)
            else -> EmptyDeploymentNodeComponent()
        }
    }

    fun updateComponent(deployment: Deployment) {
        val component = map[deployment]
        component?.update()
    }

}