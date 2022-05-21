package co.anbora.labs.ngrok.runtimes

import co.anbora.labs.ngrok.model.NgrokTunnelService

class NgrokTunnelRuntime(
    service: NgrokTunnelService, parentRuntime: NgrokApplicationRuntime
) : NgrokServiceRuntime<NgrokTunnelService>(service, parentRuntime) {

    fun publicUrl(): String = service.tunnel.publicUrl

}