package co.anbora.labs.ngrok.remote.server.deployment

import co.anbora.labs.ngrok.icons.NgrokIcons
import co.anbora.labs.ngrok.remote.server.components.NgrokDeploymentNodeComponent
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import co.anbora.labs.ngrok.runtimes.NgrokTunnelRuntime
import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure.DeploymentNodeImpl
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection
import javax.swing.JComponent

class NgrokDeploymentNode(
    project: Project?,
    connection: ServerConnection<*>,
    serverNode: ServersTreeStructure.RemoteServerNode,
    value: Deployment,
    nodeProducer: ServersTreeStructure.DeploymentNodeProducer,
    private val nodeComponentProvider: NgrokDeploymentNodeComponentProvider
) : DeploymentNodeImpl(project, connection, serverNode, value, nodeProducer) {
    private var nodeComponent: NgrokDeploymentNodeComponent? = null

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> {
        val result = ArrayList<AbstractTreeNode<Any>>()
        collectDeploymentChildren(result as List<AbstractTreeNode<*>>)
        return result
    }

    override fun update(presentation: PresentationData) {
        super.update(presentation)

        when (deployment.runtime) {
            is NgrokApplicationRuntime -> presentation.setIcon(NgrokIcons.NGROK)
            is NgrokTunnelRuntime -> presentation.setIcon(AllIcons.General.Web)
            else -> presentation.setIcon(NgrokIcons.NGROK)
        }
    }

    override fun getComponent(): JComponent? {
        if (nodeComponent == null && deployment.runtime != null) {
            nodeComponent = nodeComponentProvider.getComponent(deployment)
        }

        return nodeComponent?.getComponent()
    }
}