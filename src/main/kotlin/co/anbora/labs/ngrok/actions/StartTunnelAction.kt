package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.service.NgrokService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class StartTunnelAction(
    private val port: Int,
    private val ngrokService: NgrokService
): DumbAwareAction("Port: $port") {
    override fun actionPerformed(e: AnActionEvent) {
        ngrokService.createTunnel(port)
    }
}