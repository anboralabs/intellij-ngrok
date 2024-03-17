package co.anbora.labs.ngrok.remote.server.deployment

import co.anbora.labs.ngrok.icons.NgrokIcons
import com.intellij.remoteServer.configuration.deployment.SingletonDeploymentSourceType

class NgrokSingletonDeploymentSourceType2: SingletonDeploymentSourceType("Ngrok2", "Ngrok2", NgrokIcons.NGROK) {
    companion object {

        val instance: NgrokSingletonDeploymentSourceType = NgrokSingletonDeploymentSourceType()
    }

    override fun isEditableInDumbMode(): Boolean = true
}