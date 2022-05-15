package co.anbora.labs.ngrok.remote.server

import com.intellij.remoteServer.RemoteServerConfigurable
import javax.swing.JComponent

class NgrokHostConfigurable: RemoteServerConfigurable() {

    private val component = NgrokHostComponent()

    override fun createComponent(): JComponent = component.getPanel()

    override fun isModified(): Boolean = false

    override fun apply() = Unit
}