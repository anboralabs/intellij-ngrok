package co.anbora.labs.ngrok.service

import co.anbora.labs.ngrok.remote.server.deployment.NgrokDeploymentConfiguration
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import co.anbora.labs.ngrok.runtimes.NgrokBaseRuntime
import com.intellij.openapi.components.Service
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import java.util.concurrent.ConcurrentHashMap

@Service
class NgrokApplicationManager {

    private val applications: ConcurrentHashMap<String, NgrokApplicationRuntime> = ConcurrentHashMap()

    fun runApplication(token: String, deploymentTask: DeploymentTask<NgrokDeploymentConfiguration>): NgrokApplicationRuntime {
        return applications.getOrPut(token) {
            val newApplicationRuntime = NgrokApplicationRuntime("Ngrok Application")
            newApplicationRuntime.run(deploymentTask)
            newApplicationRuntime
        }
    }

    fun refreshApplication(token: String): List<NgrokBaseRuntime> {

        val applicationRuntime = applications.getOrPut(token) {
            NgrokApplicationRuntime("Ngrok Application")
        }

        if (!applicationRuntime.isAlive()) {
            applications.remove(token)
            return emptyList()
        }

        return applicationRuntime.refresh()
    }
}