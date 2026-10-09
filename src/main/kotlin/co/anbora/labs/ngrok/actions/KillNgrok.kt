package co.anbora.labs.ngrok.actions

import co.anbora.labs.ngrok.compatibility.ApplicationActionUtils
import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.DumbAwareAction

class KillNgrok: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokApplicationRuntime::class.java) ?: return

        ProgressManager.getInstance().run(object : Task.Backgroundable(e.project, "Stopping ngrok...") {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                runtime.shutdown()
            }
        })
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, NgrokApplicationRuntime::class.java)
        e.presentation.isVisible = runtime != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}
