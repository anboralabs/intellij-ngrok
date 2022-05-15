package co.anbora.labs.ngrok.remote.server.components

import javax.swing.JComponent
import javax.swing.JPanel

class EmptyDeploymentNodeComponent: NgrokDeploymentNodeComponent {
    private val panel: JPanel = JPanel()

    override fun getComponent(): JComponent = panel
}