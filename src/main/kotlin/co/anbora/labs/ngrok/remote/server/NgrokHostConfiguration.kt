package co.anbora.labs.ngrok.remote.server

import com.github.alexdlaird.ngrok.protocol.Region
import com.intellij.remoteServer.util.CloudConfigurationBase

class NgrokHostConfiguration : CloudConfigurationBase<NgrokHostConfiguration>() {

    var apiKey: String = ""
    var region: Region? = null
}
