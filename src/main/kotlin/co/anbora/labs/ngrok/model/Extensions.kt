package co.anbora.labs.ngrok.model

import com.github.alexdlaird.ngrok.NgrokClient
import com.github.alexdlaird.ngrok.protocol.Tunnel

fun NgrokClient.start(): NgrokClient {
    this.tunnels
    return this
}

fun Tunnel.toModel(): NgrokService {
    return NgrokTunnelService(this)
}