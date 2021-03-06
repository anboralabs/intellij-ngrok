package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.dialog.CreateTunnelDialog
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.util.ApplicationActionUtils

class AddTunnel: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokApplicationRuntime::class.java) ?: return

        val dialog = CreateTunnelDialog()
        if (dialog.showAndGet()) {
            runtime.addTunnel(
                dialog.protocol(),
                dialog.port()
            )
        }
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokApplicationRuntime::class.java)
        e.presentation.isVisible = runtime != null
    }
}