package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.service.NgrokService
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

class NgrokPopupGroup(
    val project: Project,
    val ngrokService: NgrokService = service()
) {

    private var actionGroup: DefaultActionGroup = DefaultActionGroup(null, false)
    private val ngrokActions = NgrokActions(project)

    init {
        runningTunnels().forEach {
            actionGroup.add(it)
        }
        actionGroup.add(ngrokActions)
    }

    private fun runningTunnels(): List<ActionGroup> {
        return ngrokService.tunnels().map {
            TunnelActionGroup(it)
        }
    }

    fun actionGroup(): ActionGroup = actionGroup

}