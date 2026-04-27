package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.settings.NgrokProjectSettingsConfigurable
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.ProjectManager

class Setup: NotificationAction("Setup") {

    override fun actionPerformed(
        e: AnActionEvent,
        notification: Notification
    ) {
        val project = e.project ?: ProjectManager.getInstance().defaultProject
        notification.expire()
        NgrokProjectSettingsConfigurable.show(project)
    }
}
