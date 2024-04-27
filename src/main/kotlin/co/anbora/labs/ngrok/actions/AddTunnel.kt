package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.dialog.CreateTunnelDialog
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import com.github.alexdlaird.ngrok.protocol.CreateTunnel
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.util.ApplicationActionUtils

class AddTunnel: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokApplicationRuntime::class.java) ?: return

        val dialog = CreateTunnelDialog()
        if (dialog.showAndGet()) {
            val builder = CreateTunnel.Builder()
            val host = dialog.host()
            val subdomain = dialog.subdomain()
            val hostHeader = dialog.hostHeader()

            if (!host.isNullOrBlank()) {
                builder.withHostname(host)
            }

            if (!subdomain.isNullOrBlank()) {
                builder.withSubdomain(subdomain)
            }

            if (!hostHeader.isNullOrBlank()) {
                builder.withHostHeader(hostHeader)
            }

            runtime.addTunnel(
                builder
                    .withProto(dialog.protocol())
                    .withAddr(dialog.port())
                    .build()
            )
        }
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokApplicationRuntime::class.java)
        e.presentation.isVisible = runtime != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}
