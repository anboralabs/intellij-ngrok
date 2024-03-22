package co.anbora.labs.ngrok.discovery

import co.anbora.labs.ngrok.toolchain.NgrokToolchainFlavor.Companion.getApplicableFlavors
import com.intellij.openapi.extensions.ExtensionPointName
import java.nio.file.Path

abstract class NgrokDiscoveryFlavor {

    abstract fun getPathCandidate(): Path?

    /**
     * Flavor is added to result in [getApplicableFlavors] if this method returns true.
     * @return whether this flavor is applicable.
     */
    protected open fun isApplicable(): Boolean = true

    companion object {
        private val EP_NAME: ExtensionPointName<NgrokDiscoveryFlavor> =
            ExtensionPointName.create("co.anbora.labs.ngrok.discovery")

        fun getApplicableFlavors(): List<NgrokDiscoveryFlavor> =
            EP_NAME.extensionList.filter { it.isApplicable() }
    }
}