package co.anbora.labs.ngrok.remote.server.components

import co.anbora.labs.ngrok.remote.server.components.tabs.PropertiesTab
import co.anbora.labs.ngrok.runtimes.NgrokServiceRuntime
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class TunnelDeploymentNodeComponent(private val runtime: NgrokServiceRuntime<*>) : NgrokDeploymentNodeComponent {
    private val propertiesTab: PropertiesTab

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        val tabbedPane = JBTabbedPane()

        propertiesTab = PropertiesTab(runtime.service.properties())
        tabbedPane.addTab(PropertiesTab.TITLE, propertiesTab.component)

        add(tabbedPane)
    }

    override fun getComponent(): JComponent = panel

    override fun update() {
        propertiesTab.update(runtime.service.properties())
    }
}