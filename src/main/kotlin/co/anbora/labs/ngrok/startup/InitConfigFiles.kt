package co.anbora.labs.ngrok.startup

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class InitConfigFiles: ProjectActivity {

    override suspend fun execute(project: Project) {
        val path = NgrokDiscovery.ngrokPath
        if (path == null) {

        }
    }
}
