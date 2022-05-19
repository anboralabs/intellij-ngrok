package co.anbora.labs.ngrok.runtimes

import co.anbora.labs.ngrok.model.NgrokService
import co.anbora.labs.ngrok.model.NgrokTunnelService
import co.anbora.labs.ngrok.model.toModel
import com.github.alexdlaird.exception.NgrokException
import com.github.alexdlaird.ngrok.NgrokClient
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig
import com.github.alexdlaird.ngrok.protocol.CreateTunnel
import com.github.alexdlaird.ngrok.protocol.Proto
import com.intellij.openapi.diagnostic.logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class NgrokApplicationRuntime(applicationName: String) : NgrokBaseRuntime(applicationName) {

    private val log = logger<NgrokApplicationRuntime>()
    private val serviceRuntimes: MutableMap<String, NgrokServiceRuntime<NgrokService>> = mutableMapOf()

    private var ngrokClient: NgrokClient? = null
    private val configBuilder = JavaNgrokConfig.Builder()

    fun run(apiToken: String) {

        ngrokClient = NgrokClient.Builder()
            .withJavaNgrokConfig(configBuilder.withAuthToken(apiToken).build())
            .build()
    }

    fun addTunnel() {
        ngrokClient?.connect(
            CreateTunnel.Builder()
                .withProto(Proto.HTTP)
                .withAddr(3000)
                .build()
        )
    }

    fun properties(): MutableMap<String, String?> {
        return mutableMapOf(
            "Version" to ngrokClient?.version?.ngrokVersion,
            "Path" to ngrokClient?.javaNgrokConfig?.ngrokPath?.toString(),
            "Token" to ngrokClient?.javaNgrokConfig?.authToken
        )
    }

    fun shutdown() {
        ngrokClient?.kill()
    }

    fun isAlive(): Boolean = ngrokClient?.ngrokProcess?.isRunning ?: false

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
        try {
            val tunnelsDto = ngrokClient?.tunnels ?: emptyList()
            return@runBlocking tunnelsDto.mapNotNull { it.toModel() }
        } catch (e: NgrokException) {
            return@runBlocking emptyList<NgrokService>()
        }
    }
}