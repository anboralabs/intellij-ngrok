package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.settings.NgrokProjectSettingsConfigurable
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.ProjectManager

class Setup: DumbAwareAction("Setup") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: ProjectManager.getInstance().defaultProject
        NgrokProjectSettingsConfigurable.show(project)
    }
}