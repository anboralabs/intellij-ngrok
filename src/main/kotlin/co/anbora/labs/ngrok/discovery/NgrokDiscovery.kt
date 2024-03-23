package co.anbora.labs.ngrok.discovery

import co.anbora.labs.ngrok.settings.NgrokConfigurationUtil
import co.anbora.labs.ngrok.utils.toPathOrNull
import com.github.alexdlaird.ngrok.installer.NgrokInstaller
import java.nio.file.Path

object NgrokDiscovery {

    val ngrokEmbeddedPath: Path by lazy {
        NgrokInstaller.DEFAULT_NGROK_PATH.parent
    }

    val ngrokUnixPath by lazy {
        NgrokConfigurationUtil.executeAndReturnOutput("which", "ngrok").toPathOrNull()
    }

    val ngrokWindowsPath by lazy {
        NgrokConfigurationUtil.executeAndReturnOutput("where", "ngrok.exe").toPathOrNull()
    }

}
