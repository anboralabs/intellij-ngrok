package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.background.InstallNgrokTask
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.ProjectManager

class Install: NotificationAction("Download") {
    override fun actionPerformed(
        e: AnActionEvent,
        notification: Notification
    ) {
        val project = e.project ?: ProjectManager.getInstance().defaultProject
        notification.expire()
        ProgressManager.getInstance().run(InstallNgrokTask(project))
    }
}
