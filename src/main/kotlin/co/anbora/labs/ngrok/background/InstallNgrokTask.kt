package co.anbora.labs.ngrok.background

import co.anbora.labs.ngrok.discovery.NgrokDiscovery
import co.anbora.labs.ngrok.toolchain.NgrokKnownToolchainsState
import co.anbora.labs.ngrok.toolchain.NgrokToolchain
import co.anbora.labs.ngrok.toolchain.NgrokToolchainService
import com.github.alexdlaird.ngrok.installer.NgrokInstaller
import com.github.alexdlaird.ngrok.installer.NgrokVersion
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class InstallNgrokTask(
    private val project: Project
): Task.Backgroundable(project, "Downloading ngrok...") {
    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = true

        val ngrokPathInstall = NgrokDiscovery.ngrokEmbeddedPath

        val installer = NgrokInstaller()
        installer.installNgrok(ngrokPathInstall, NgrokVersion.V2)

        val knownToolchain = NgrokKnownToolchainsState.getInstance()

        if (knownToolchain.isKnown(ngrokPathInstall.toString())) {
            return
        }

        val toolchain = NgrokToolchain.fromPath(ngrokPathInstall.toString())

        knownToolchain.add(toolchain)

        val toolchainSettings = NgrokToolchainService.toolchainSettings
        toolchainSettings.setToolchain(toolchain)


    }
}