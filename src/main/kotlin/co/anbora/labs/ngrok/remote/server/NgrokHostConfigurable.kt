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
        component.setRegion(myConfiguration.region)
    }

    override fun isModified(): Boolean = component.getApiToken() != myConfiguration.apiKey || component.getRegion() != myConfiguration.region

    override fun apply() {
        myConfiguration.apiKey = component.getApiToken()
        myConfiguration.region = component.getRegion()
    }

    override fun canCheckConnection(): Boolean = false
}