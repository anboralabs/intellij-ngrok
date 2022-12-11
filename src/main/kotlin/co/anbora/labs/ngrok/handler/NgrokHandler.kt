package co.anbora.labs.ngrok.handler

import co.anbora.labs.ngrok.model.start
import co.anbora.labs.ngrok.notifications.Notifier.notifyError
import co.anbora.labs.ngrok.remote.server.NgrokHostConfiguration
import com.github.alexdlaird.exception.JavaNgrokHTTPException
import com.github.alexdlaird.exception.NgrokException
import com.github.alexdlaird.ngrok.NgrokClient
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig
import com.github.alexdlaird.ngrok.protocol.CreateTunnel
import com.github.alexdlaird.ngrok.protocol.Tunnel
import com.jayway.jsonpath.JsonPath

class NgrokHandler(
    private var ngrokClient: NgrokClient? = null,
    private var configBuilder: JavaNgrokConfig.Builder = JavaNgrokConfig.Builder()
) {

    fun start(configuration: NgrokHostConfiguration) {
        val region = configuration.region
        if (region != null) {
            configBuilder.withRegion(region)
        }

        executeWithNotification {
            ngrokClient = NgrokClient.Builder()
                .withJavaNgrokConfig(configBuilder.withAuthToken(configuration.apiKey).build())
                .build()
                .start()
        }
    }

    fun tunnels(): List<Tunnel> {
        return executeWithDefaultValue({
            ngrokClient?.tunnels ?: emptyList()
        }, emptyList())
    }

    fun addTunnel(tunnel: CreateTunnel) {
        executeWithNotification {
            ngrokClient?.connect(tunnel)
        }
    }

    fun disconnect(publicUrl: String) {
        executeWithNotification {
            ngrokClient?.disconnect(publicUrl)
        }
    }

    fun isAlive(): Boolean = ngrokClient?.ngrokProcess?.isRunning ?: false

    fun kill() = ngrokClient?.kill()

    fun properties(): MutableMap<String, String?> {
        return mutableMapOf(
            "Version" to ngrokClient?.version?.ngrokVersion,
            "Path" to ngrokClient?.javaNgrokConfig?.ngrokPath?.toString(),
            "Token" to ngrokClient?.javaNgrokConfig?.authToken
        )
    }

    private fun executeWithNotification(process: () -> Unit) {
        try {
            process()
        } catch (ex: JavaNgrokHTTPException) {
            val message = processException(ex)
            notifyError(content = message)
        }
    }

    private fun <T> executeWithDefaultValue(process: () -> T, default: T): T {
        return try {
            process()
        } catch (ex: NgrokException) {
            default
        }
    }

    private fun processException(ex: JavaNgrokHTTPException): String {
        return try {
            val jsonPath = JsonPath.parse(ex.body)
            val message = jsonPath.read<String>("$.msg")
            val detail = jsonPath.read<String>("$.details.err")

            if (detail == "EOF") {
                message
            } else "$message: $detail"
        } catch (ex: Exception) {
            ex.message.orEmpty()
        }
    }

}