package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.ui.Icons
import com.github.alexdlaird.ngrok.protocol.Tunnel
import com.intellij.dvcs.ui.CustomIconProvider
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import javax.swing.Icon

class TunnelActionGroup(
    private val tunnel: Tunnel
): ActionGroup(), DumbAware, CustomIconProvider {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return listOf(
            CopyToClipboardAction(),
            StopTunnelAction()
        ).toTypedArray()
    }

    override fun getRightIcon(): Icon? = Icons.Title
}