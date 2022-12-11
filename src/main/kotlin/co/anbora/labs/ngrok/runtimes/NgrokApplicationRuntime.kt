package co.anbora.labs.ngrok.runtimes

import co.anbora.labs.ngrok.handler.NgrokHandler
import co.anbora.labs.ngrok.model.NgrokService
import co.anbora.labs.ngrok.model.NgrokTunnelService
import co.anbora.labs.ngrok.model.toModel
import co.anbora.labs.ngrok.remote.server.NgrokHostConfiguration
import com.github.alexdlaird.ngrok.protocol.CreateTunnel
import com.intellij.openapi.diagnostic.logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class NgrokApplicationRuntime(applicationName: String) : NgrokBaseRuntime(applicationName) {

    private val log = logger<NgrokApplicationRuntime>()
    private val serviceRuntimes: MutableMap<String, NgrokServiceRuntime<NgrokService>> = mutableMapOf()

    private val ngrokHandler = NgrokHandler()

    fun run(configuration: NgrokHostConfiguration) = ngrokHandler.start(configuration)

    fun addTunnel(tunnel: CreateTunnel) = ngrokHandler.addTunnel(tunnel)

    fun disconnectTunnel(publicUrl: String) = ngrokHandler.disconnect(publicUrl)

    fun properties(): MutableMap<String, String?> = ngrokHandler.properties()

    fun shutdown() = ngrokHandler.kill()

    fun isAlive(): Boolean = ngrokHandler.isAlive()

    fun waitForReadiness() = runBlocking {
        try {
            for (i in 1..20) {
                if (isAlive()) {
                    break
                } else {
                    delay(500)
                }
            }
        } catch (ex: Exception) {
            log.error(ex.message)
        }
    }

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
        ngrokHandler.tunnels().map { it.toModel() }
    }
}