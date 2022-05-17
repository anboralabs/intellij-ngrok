package co.anbora.labs.ngrok.model

import com.github.alexdlaird.ngrok.protocol.Tunnel

class NgrokTunnelService(
    tunnel: Tunnel
): NgrokService(tunnel) {
    override fun getName(): String = tunnel.name
}