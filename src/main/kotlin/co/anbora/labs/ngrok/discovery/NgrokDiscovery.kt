package co.anbora.labs.ngrok.discovery

import co.anbora.labs.ngrok.settings.NgrokConfigurationUtil
import co.anbora.labs.ngrok.utils.toPathOrNull
import com.intellij.openapi.application.PathManager
import java.nio.file.Path
import java.nio.file.Paths

object NgrokDiscovery {

    val ngrokEmbeddedPath: Path by lazy {
        Paths.get(PathManager.getConfigPath(), "plugins", "embedded", "ngrok")
    }

    val ngrokUnixPath by lazy {
        NgrokConfigurationUtil.executeAndReturnOutput("which", "ngrok").toPathOrNull()
    }

    val ngrokWindowsPath by lazy {
        NgrokConfigurationUtil.executeAndReturnOutput("where", "ngrok.exe").toPathOrNull()
    }

}