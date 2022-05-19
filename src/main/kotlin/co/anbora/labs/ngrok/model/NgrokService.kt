package co.anbora.labs.ngrok.model

import com.github.alexdlaird.ngrok.protocol.Tunnel

sealed class NgrokService(
    val tunnel: Tunnel
) {

    abstract fun getName(): String

    abstract fun properties(): MutableMap<String, String?>
}