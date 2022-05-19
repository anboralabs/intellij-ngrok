package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.util.ApplicationActionUtils

class AddTunnel: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokApplicationRuntime::class.java) ?: return

        runtime.addTunnel()
    }
}