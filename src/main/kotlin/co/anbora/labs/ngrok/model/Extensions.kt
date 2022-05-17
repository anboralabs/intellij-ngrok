package co.anbora.labs.ngrok.model

import com.github.alexdlaird.ngrok.protocol.Tunnel

fun Tunnel.toModel(): NgrokService {
    return NgrokTunnelService(this)
}