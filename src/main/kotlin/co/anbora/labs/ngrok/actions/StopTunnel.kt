package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.compatibility.ApplicationActionUtils
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import co.anbora.labs.ngrok.runtimes.NgrokTunnelRuntime
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.DumbAwareAction

class StopTunnel: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokTunnelRuntime::class.java) ?: return

        val parent = runtime.parent as? NgrokApplicationRuntime ?: return
        val publicUrl = runtime.publicUrl()

        ProgressManager.getInstance().run(object : Task.Backgroundable(e.project, "Stopping ngrok tunnel...") {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                parent.disconnectTunnel(publicUrl)
            }
        })
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokTunnelRuntime::class.java)
        e.presentation.isVisible = runtime != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}
