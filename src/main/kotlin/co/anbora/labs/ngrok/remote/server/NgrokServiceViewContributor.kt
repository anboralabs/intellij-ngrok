package co.anbora.labs.ngrok.remote.server

import co.anbora.labs.ngrok.icons.NgrokIcons
import co.anbora.labs.ngrok.remote.server.deployment.NgrokDeploymentNode
import co.anbora.labs.ngrok.remote.server.deployment.NgrokDeploymentNodeComponentProvider
import co.anbora.labs.ngrok.service.ProjectDisposable
import com.intellij.execution.services.ServiceViewDescriptor
import com.intellij.execution.services.ServiceViewLazyContributor
import com.intellij.execution.services.SimpleServiceViewDescriptor
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection

class NgrokServiceViewContributor: RemoteServersServiceViewContributor(), ServiceViewLazyContributor {

    companion object {
        private val serviceViewDescriptor = SimpleServiceViewDescriptor("Ngrok", NgrokIcons.NGROK)
    }

    private val componentProviders: MutableMap<Project, NgrokDeploymentNodeComponentProvider> = mutableMapOf()


    override fun getViewDescriptor(project: Project): ServiceViewDescriptor = serviceViewDescriptor

    override fun createDeploymentNode(
        connection: ServerConnection<*>?,
        serverNode: ServersTreeStructure.RemoteServerNode?,
        deployment: Deployment?
    ): AbstractTreeNode<*> {
        val project = serverNode!!.project!!

        val nodeComponentProvider = componentProviders.getOrPut(project) {
            val provider = NgrokDeploymentNodeComponentProvider(project)
            Disposer.register(project.service<ProjectDisposable>()) {
                componentProviders.remove(project)
            }
            return@getOrPut provider
        }

        nodeComponentProvider.updateComponent(deployment!!)

        return NgrokDeploymentNode(
            project,
            connection!!,
            serverNode,
            deployment,
            this,
            nodeComponentProvider
        )
    }

    override fun accept(server: RemoteServer<*>): Boolean = server.type == NgrokHostType.getInstance()

    override fun selectLog(deploymentNode: AbstractTreeNode<*>, logName: String) = Unit

    override fun getActionGroups(): ActionGroups = ActionGroups(
        "Ngrok.RemoteServers.Main",
        "Ngrok.RemoteServers.Secondary",
        "Ngrok.RemoteServers.Toolbar"
    )
}