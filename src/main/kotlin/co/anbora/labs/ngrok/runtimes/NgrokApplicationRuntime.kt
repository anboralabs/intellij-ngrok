package co.anbora.labs.ngrok.runtimes

import co.anbora.labs.ngrok.model.NgrokService
import co.anbora.labs.ngrok.model.NgrokTunnelService
import co.anbora.labs.ngrok.model.toModel
import co.anbora.labs.ngrok.remote.server.deployment.NgrokDeploymentConfiguration
import com.github.alexdlaird.exception.NgrokException
import com.github.alexdlaird.ngrok.NgrokClient
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import kotlinx.coroutines.runBlocking

class NgrokApplicationRuntime(applicationName: String) : NgrokBaseRuntime(applicationName) {

    private val serviceRuntimes: MutableMap<String, NgrokServiceRuntime<NgrokService>> = mutableMapOf()

    private var ngrokClient: NgrokClient? = null
    private val configBuilder = JavaNgrokConfig.Builder()

    fun run(deploymentTask: DeploymentTask<NgrokDeploymentConfiguration>) {

        ngrokClient = NgrokClient.Builder()
            .withJavaNgrokConfig(configBuilder.withAuthToken(deploymentTask.configuration.apiKey).build())
            .build()
    }

    fun isAlive(): Boolean = ngrokClient?.ngrokProcess?.isRunning ?: false

    fun refresh(): List<NgrokBaseRuntime> {
        val tunnels = getTunnels()
        refreshServiceRuntimes(tunnels)

        val runtimes = mutableListOf<NgrokBaseRuntime>()
        runtimes.add(this)
        runtimes.addAll(serviceRuntimes.values)

        return runtimes.toList()
    }

    private fun refreshServiceRuntimes(tunnels: List<NgrokService>) {
        val currentTunnelNames = serviceRuntimes.keys.toSet()
        val updatedTunnelNames = tunnels.map { it.getName() }.toSet()

        for (updatedTunnel in tunnels) {
            val serviceName = updatedTunnel.getName()
            val serviceRuntime = serviceRuntimes[serviceName]
            if (serviceRuntime != null) {
                //serviceRuntime.update(updatedTunnel)
            } else {
                val newRuntime = createTunnelRuntime(updatedTunnel, this)
                serviceRuntimes[serviceName] = newRuntime
            }
        }

        for (deletedServiceName in currentTunnelNames.subtract(updatedTunnelNames)) {
            serviceRuntimes.remove(deletedServiceName)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createTunnelRuntime(
        model: NgrokService,
        parent: NgrokApplicationRuntime
    ): NgrokServiceRuntime<NgrokService> =
        when (model) {
            is NgrokTunnelService -> NgrokTunnelRuntime(model, parent) as NgrokServiceRuntime<NgrokService>
        }

    private fun getTunnels(): List<NgrokService> = runBlocking {
        try {
            val tunnelsDto = ngrokClient?.tunnels ?: emptyList()
            return@runBlocking tunnelsDto.mapNotNull { it.toModel() }
        } catch (e: NgrokException) {
            return@runBlocking emptyList<NgrokService>()
        }
    }
}