package co.anbora.labs.ngrok.discovery.flavor

import co.anbora.labs.ngrok.discovery.NgrokDiscovery
import co.anbora.labs.ngrok.discovery.NgrokDiscoveryFlavor
import com.intellij.openapi.util.SystemInfo
import java.nio.file.Path

class NgrokUnixDiscovery: NgrokDiscoveryFlavor() {
    override fun getPathCandidate(): Path? {
        return NgrokDiscovery.ngrokUnixPath
    }

    override fun isApplicable(): Boolean = SystemInfo.isUnix
}
