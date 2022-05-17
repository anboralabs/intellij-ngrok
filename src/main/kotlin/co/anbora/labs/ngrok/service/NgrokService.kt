package co.anbora.labs.ngrok.service

import com.github.alexdlaird.ngrok.NgrokClient
import com.github.alexdlaird.ngrok.protocol.CreateTunnel
import com.github.alexdlaird.ngrok.protocol.Proto
import com.github.alexdlaird.ngrok.protocol.Tunnel
import com.github.alexdlaird.ngrok.protocol.Version
import com.intellij.openapi.Disposable

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

    fun version(): Version = ngrokClient.version

    override fun dispose() {
        ngrokClient.kill()
    }
}