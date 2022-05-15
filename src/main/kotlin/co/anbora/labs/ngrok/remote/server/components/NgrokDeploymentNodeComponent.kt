package co.anbora.labs.ngrok.remote.server.components

import javax.swing.JComponent

interface NgrokDeploymentNodeComponent {
    fun getComponent(): JComponent
    fun update() {}
}

