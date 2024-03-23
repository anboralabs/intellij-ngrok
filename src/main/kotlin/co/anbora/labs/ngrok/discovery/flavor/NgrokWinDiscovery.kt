package co.anbora.labs.ngrok.discovery.flavor

import co.anbora.labs.ngrok.discovery.NgrokDiscovery
import co.anbora.labs.ngrok.discovery.NgrokDiscoveryFlavor
import com.intellij.openapi.util.SystemInfo
import java.nio.file.Path

class NgrokWinDiscovery: NgrokDiscoveryFlavor() {
    override fun getPathCandidate(): Path? {
        return NgrokDiscovery.ngrokWindowsPath
    }

    override fun isApplicable(): Boolean = SystemInfo.isWindows
}
