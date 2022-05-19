package co.anbora.labs.ngrok.remote.server.components

import co.anbora.labs.ngrok.remote.server.components.tabs.PropertiesTab
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import co.anbora.labs.ngrok.service.ProjectDisposable
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ApplicationDeploymentNodeComponent(private val project: Project, private val runtime: NgrokApplicationRuntime) :
    NgrokDeploymentNodeComponent, Disposable {
    private val panel: JPanel
    private val propertiesTab: PropertiesTab

    init {
        panel = JPanel().apply {
            layout = BorderLayout()

            propertiesTab = PropertiesTab(runtime.properties())
            val tabbedPane = JBTabbedPane().apply {
                addTab(PropertiesTab.TITLE, propertiesTab.component)
            }
            add(tabbedPane)
        }

        Disposer.register(project.service<ProjectDisposable>(), this)
    }

    override fun update() {
        propertiesTab.update(runtime.properties())
    }

    override fun getComponent(): JComponent = panel

    override fun dispose() {
    }
}