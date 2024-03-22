package co.anbora.labs.ngrok.discovery

import co.anbora.labs.ngrok.settings.NgrokConfigurationUtil
import co.anbora.labs.ngrok.utils.toPathOrNull

object NgrokDiscovery {

    val ngrokUnixPath by lazy {
        NgrokConfigurationUtil.executeAndReturnOutput("which", "ngrok").toPathOrNull()
    }

    val ngrokWindowsPath by lazy {
        NgrokConfigurationUtil.executeAndReturnOutput("where", "ngrok.exe").toPathOrNull()
    }

}