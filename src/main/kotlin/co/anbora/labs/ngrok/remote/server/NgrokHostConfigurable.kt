package co.anbora.labs.ngrok.remote.server

import com.intellij.remoteServer.RemoteServerConfigurable
import javax.swing.JComponent

class NgrokHostConfigurable(
    private val myConfiguration: NgrokHostConfiguration
): RemoteServerConfigurable() {

    private val component = NgrokHostComponent()

    override fun createComponent(): JComponent = component.getPanel()

    override fun reset() {
        component.setApiToken(myConfiguration.apiKey)
    }

    override fun isModified(): Boolean = component.getApiToken() != myConfiguration.apiKey

    override fun apply() {
        myConfiguration.apiKey = component.getApiToken()
    }

    override fun canCheckConnection(): Boolean = false
}