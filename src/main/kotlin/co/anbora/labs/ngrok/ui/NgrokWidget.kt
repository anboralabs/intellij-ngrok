package co.anbora.labs.ngrok.ui

import co.anbora.labs.ngrok.actions.NgrokPopupGroup
import com.intellij.ide.DataManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget.TextPresentation
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.popup.PopupFactoryImpl.ActionGroupPopup
import com.intellij.util.Consumer
import java.awt.KeyboardFocusManager
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.Icon

class NgrokWidget(
    project: Project
): EditorBasedWidget(project), StatusBarWidget.IconPresentation, TextPresentation {

    private val actionGroup = NgrokPopupGroup(project)

    override fun getTooltipText(): String = "Click for details"

    override fun getClickConsumer(): Consumer<MouseEvent> {
        return Consumer {
            val popup = widgetActions()
            val dimension = popup.content.preferredSize
            val at = Point(0, -dimension.height)
            popup.show(RelativePoint(it.component, at))
        }
    }

    private fun widgetActions(): ListPopup {
        var focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().focusOwner
        if (focusOwner == null) {
            val focusManager = IdeFocusManager.getInstance(project)
            val frame = focusManager.lastFocusedIdeWindow
            if (frame != null) {
                focusOwner = focusManager.getLastFocusedFor(frame)
            }
        }
        val dataContext = DataManager.getInstance().getDataContext(focusOwner)

        return ActionGroupPopup(
            "Tunnels", actionGroup.actionGroup(), dataContext, false, false, false, true, null, -1,
            null, null
        )
    }

    override fun getIcon(): Icon? = Icons.Status

    override fun getText(): String = "Ngrok"

    override fun getAlignment(): Float = 0f

    override fun ID(): String = "ngrok-widget"

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

}