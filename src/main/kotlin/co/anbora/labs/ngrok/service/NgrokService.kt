package co.anbora.labs.ngrok.service

import co.anbora.labs.ngrok.remote.server.NgrokHostConfiguration
import com.github.alexdlaird.ngrok.NgrokClient
import com.github.alexdlaird.ngrok.protocol.CreateTunnel
import com.github.alexdlaird.ngrok.protocol.Proto
import com.github.alexdlaird.ngrok.protocol.Tunnel
import com.intellij.openapi.Disposable
import com.intellij.remoteServer.util.CloudConfigurationBase

class NgrokService: Disposable {

    private val ngrokClient = NgrokClient.Builder().build()

    fun tunnels(): List<Tunnel> = ngrokClient.tunnels

    fun createTunnel(port: Int) {
        ngrokClient.connect(
            CreateTunnel.Builder()
                .withProto(Proto.HTTP)
                .withAddr(port)
                .build()
        )
    }

    override fun dispose() {
        ngrokClient.kill()
    }
}