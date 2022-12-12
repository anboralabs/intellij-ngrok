package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.remote.server.NgrokHostType
import co.anbora.labs.ngrok.remote.server.NgrokServiceViewContributor
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor

class AddNgrokHostAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        RemoteServersServiceViewContributor.addNewRemoteServer(project,
                NgrokHostType.getInstance(),
                NgrokServiceViewContributor::class.java)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.text = "Ngrok Host"
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}
