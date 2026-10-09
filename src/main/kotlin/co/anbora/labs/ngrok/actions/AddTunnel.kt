package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.compatibility.ApplicationActionUtils
import co.anbora.labs.ngrok.dialog.CreateTunnelDialog
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import com.github.alexdlaird.ngrok.protocol.CreateTunnel
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.DumbAwareAction

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

            val createTunnel = builder
                .withProto(dialog.protocol())
                .withAddr(dialog.port())
                .build()

            ProgressManager.getInstance().run(object : Task.Backgroundable(e.project, "Starting ngrok tunnel...") {
                override fun run(indicator: ProgressIndicator) {
                    indicator.isIndeterminate = true
                    runtime.addTunnel(createTunnel)
                }
            })
        }
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokApplicationRuntime::class.java)
        e.presentation.isVisible = runtime != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}
