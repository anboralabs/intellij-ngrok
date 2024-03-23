package co.anbora.labs.ngrok.startup

import co.anbora.labs.ngrok.actions.Install
import co.anbora.labs.ngrok.actions.Setup
import co.anbora.labs.ngrok.discovery.NgrokDiscoveryFlavor
import co.anbora.labs.ngrok.notifications.NgrokNotifications
import co.anbora.labs.ngrok.toolchain.NgrokToolchainService
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.util.logging.Logger

class InitConfigFiles: ProjectActivity {

    private val logger = Logger.getLogger(InitConfigFiles::class.simpleName)

    override suspend fun execute(project: Project) {
        val path = NgrokDiscoveryFlavor.getApplicableFlavors().firstOrNull()?.getPathCandidate()
        if (path != null) {
            logger.info {
                "toolchain path: $path"
            }
        }

        val toolchainSettings = NgrokToolchainService.toolchainSettings
        if (!toolchainSettings.toolchain().isValid()) {
            val notification = NgrokNotifications.createNotification(
                "Ngrok Plugin",
                "Please setup ngrok",
                NotificationType.INFORMATION,
                Install(),
                Setup()
            )

            NgrokNotifications.showNotification(notification, project)
        }
    }
}
