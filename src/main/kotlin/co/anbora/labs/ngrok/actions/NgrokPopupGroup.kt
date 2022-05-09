package co.anbora.labs.ngrok.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project

class NgrokPopupGroup(
    val project: Project
) {

    private var actionGroup: DefaultActionGroup = DefaultActionGroup(null, false)
    private val ngrokActions = NgrokActions(project)

    init {
        actionGroup.add(ngrokActions)
    }

    fun actionGroup(): ActionGroup = actionGroup

}