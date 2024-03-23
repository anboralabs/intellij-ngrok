package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.background.InstallNgrokTask
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.ProjectManager

class Install: DumbAwareAction("Download") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: ProjectManager.getInstance().defaultProject
        ProgressManager.getInstance().run(InstallNgrokTask(project))
    }
}
