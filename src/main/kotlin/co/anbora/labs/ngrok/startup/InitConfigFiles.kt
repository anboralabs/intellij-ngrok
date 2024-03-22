package co.anbora.labs.ngrok.startup

import co.anbora.labs.ngrok.discovery.NgrokDiscoveryFlavor
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
    }
}
