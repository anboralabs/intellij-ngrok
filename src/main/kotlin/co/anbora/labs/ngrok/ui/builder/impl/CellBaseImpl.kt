package co.anbora.labs.ngrok.ui.builder.impl

import co.anbora.labs.ngrok.ui.builder.CellBase
import co.anbora.labs.ngrok.ui.builder.RightGap
import co.anbora.labs.ngrok.ui.gridLayout.Gaps
import co.anbora.labs.ngrok.ui.gridLayout.HorizontalAlign
import co.anbora.labs.ngrok.ui.gridLayout.VerticalAlign
import com.intellij.ui.layout.ComponentPredicate

sealed class CellBaseImpl<T : CellBase<T>> : CellBase<T> {

    var horizontalAlign = HorizontalAlign.LEFT
        private set

    var verticalAlign = VerticalAlign.CENTER
        private set

    var resizableColumn = false
        private set

    var rightGap: RightGap? = null
        private set

    var customGaps: Gaps? = null
        private set

    abstract fun visibleFromParent(parentVisible: Boolean)

    abstract fun enabledFromParent(parentEnabled: Boolean)

    override fun visibleIf(predicate: ComponentPredicate): CellBase<T> {
        visible(predicate())
        predicate.addListener { visible(it) }
        return this
    }

    override fun enabledIf(predicate: ComponentPredicate): CellBase<T> {
        enabled(predicate())
        predicate.addListener { enabled(it) }
        return this
    }

    override fun horizontalAlign(horizontalAlign: HorizontalAlign): CellBase<T> {
        this.horizontalAlign = horizontalAlign
        return this
    }

    override fun verticalAlign(verticalAlign: VerticalAlign): CellBase<T> {
        this.verticalAlign = verticalAlign
        return this
    }

    override fun resizableColumn(): CellBase<T> {
        this.resizableColumn = true
        return this
    }

    override fun gap(rightGap: RightGap): CellBase<T> {
        this.rightGap = rightGap
        return this
    }

    override fun customize(customGaps: Gaps): CellBase<T> {
        this.customGaps = customGaps
        return this
    }
}