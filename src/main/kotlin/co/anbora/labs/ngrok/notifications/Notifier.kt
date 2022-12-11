package co.anbora.labs.ngrok.notifications

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifier {

    fun notifyError(
        project: Project? = null,
        content: String
    ) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("ngrok.notification")
            .createNotification(content, NotificationType.ERROR)
            .notify(project)
    }
}