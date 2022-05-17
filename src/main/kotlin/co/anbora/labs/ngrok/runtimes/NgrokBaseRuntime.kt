package co.anbora.labs.ngrok.runtimes

import co.anbora.labs.ngrok.remote.server.NgrokHostType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.remoteServer.ServerType
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentRuntime
import com.intellij.remoteServer.util.AgentTaskExecutor
import com.intellij.remoteServer.util.CloudApplicationRuntime

sealed class NgrokBaseRuntime(applicationName: String?) : CloudApplicationRuntime(applicationName) {
    protected var parent: NgrokBaseRuntime? = null

    override fun getCloudType(): ServerType<*> = NgrokHostType.getInstance()

    override fun isUndeploySupported(): Boolean = false

    override fun undeploy(callback: UndeploymentTaskCallback) {
        throw UnsupportedOperationException()
    }

    override fun getTaskExecutor(): ServerTaskExecutor {
        throw UnsupportedOperationException()
    }

    override fun getAgentTaskExecutor(): AgentTaskExecutor {
        throw UnsupportedOperationException()
    }

    override fun getParent(): DeploymentRuntime? = parent

    open fun getSourceFile(): VirtualFile? = null
}