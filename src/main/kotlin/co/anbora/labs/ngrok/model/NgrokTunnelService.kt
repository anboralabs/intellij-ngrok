package co.anbora.labs.ngrok.model

import com.github.alexdlaird.ngrok.protocol.Tunnel

class NgrokTunnelService(
    tunnel: Tunnel
): NgrokService(tunnel) {
    override fun getName(): String = tunnel.name

    override fun properties(): MutableMap<String, String?> {
        return mutableMapOf(
            "Name" to tunnel.name,
            "Url" to tunnel.publicUrl,
            "Uri" to tunnel.uri,
            "Protocol" to tunnel.proto,
            "Forwarded" to tunnel.config.addr
        )
    }
}