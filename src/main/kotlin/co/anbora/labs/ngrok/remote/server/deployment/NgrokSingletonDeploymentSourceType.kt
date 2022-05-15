package co.anbora.labs.ngrok.remote.server.deployment

import co.anbora.labs.ngrok.icons.NgrokIcons
import com.intellij.remoteServer.configuration.deployment.SingletonDeploymentSourceType

class NgrokSingletonDeploymentSourceType: SingletonDeploymentSourceType("Ngrok", "Ngrok", NgrokIcons.NGROK) {
    companion object{
        fun getInstance(): NgrokSingletonDeploymentSourceType {
            return findExtension(NgrokSingletonDeploymentSourceType::class.java) as NgrokSingletonDeploymentSourceType
        }
    }

    override fun isEditableInDumbMode(): Boolean = true
}