package co.anbora.labs.ngrok.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAwareAction

class RestartIDEAction: DumbAwareAction("Restart") {
    override fun actionPerformed(p0: AnActionEvent) {
        ApplicationManager.getApplication().restart()
    }
}
