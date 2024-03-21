package co.anbora.labs.ngrok.toolchain.flavor

import co.anbora.labs.ngrok.startup.NgrokDiscovery
import co.anbora.labs.ngrok.toolchain.NgrokToolchainFlavor
import com.intellij.openapi.util.SystemInfo
import java.nio.file.Path

class NgrokUnixWhichPathToolchain: NgrokToolchainFlavor() {

    override fun getHomePathCandidates(): Sequence<Path> {
        val path = NgrokDiscovery.ngrokPath
        if (path != null) {
            return listOf(path.parent).asSequence()
        }
        return emptySequence()
    }

    override fun isApplicable(): Boolean = SystemInfo.isUnix
}