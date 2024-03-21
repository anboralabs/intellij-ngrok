package co.anbora.labs.ngrok.startup

import co.anbora.labs.ngrok.settings.NgrokConfigurationUtil
import co.anbora.labs.ngrok.utils.toPathOrNull

object NgrokDiscovery {

    val ngrokPath by lazy {
        NgrokConfigurationUtil.executeAndReturnOutput("which", "ngrok").toPathOrNull()
    }

}