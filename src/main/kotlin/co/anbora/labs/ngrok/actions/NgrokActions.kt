package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.service.NgrokService
import co.anbora.labs.ngrok.settings.NgrokSettings
import co.anbora.labs.ngrok.ui.Icons
import com.intellij.dvcs.ui.CustomIconProvider
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import javax.swing.Icon

class NgrokActions(
    project: Project,
    val settings: NgrokSettings = service(),
    val ngrokService: NgrokService = service()
): ActionGroup(), DumbAware, CustomIconProvider, AlwaysVisibleActionGroup {

    override fun getRightIcon(): Icon? = Icons.Title

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return listOf(
            Separator("Create on:"),
            *defaultPorts().toTypedArray()
        ).toTypedArray()
    }

    private fun defaultPorts(): List<DumbAwareAction> {
        return settings.ports.map {
            StartTunnelAction(it, ngrokService)
        }.toList()
    }

}