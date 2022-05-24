package co.anbora.labs.ngrok.ui.builder

import co.anbora.labs.ngrok.ui.gridLayout.Gaps
import co.anbora.labs.ngrok.ui.gridLayout.HorizontalAlign
import co.anbora.labs.ngrok.ui.gridLayout.VerticalAlign
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.layout.ComponentPredicate
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Nls
import java.awt.Color
import javax.swing.JLabel

val EMPTY_LABEL = String()

interface Panel : CellBase<Panel> {

    override fun visible(isVisible: Boolean): Panel

    override fun visibleIf(predicate: ComponentPredicate): Panel

    override fun enabled(isEnabled: Boolean): Panel

    override fun enabledIf(predicate: ComponentPredicate): Panel

    override fun horizontalAlign(horizontalAlign: HorizontalAlign): Panel

    override fun verticalAlign(verticalAlign: VerticalAlign): Panel

    override fun resizableColumn(): Panel

    override fun gap(rightGap: RightGap): Panel

    override fun customize(customGaps: Gaps): Panel

    /**
     * Adds row with [RowLayout.LABEL_ALIGNED] layout and [label]. Use [EMPTY_LABEL] for empty label.
     * Do not use row(""), because it creates unnecessary label component in layout
     */
    fun row(@Nls label: String, init: Row.() -> Unit): Row

    /**
     * Adds row with [RowLayout.LABEL_ALIGNED] layout and [label]. If label is null then
     * [RowLayout.INDEPENDENT] layout is used
     */
    fun row(label: JLabel? = null, init: Row.() -> Unit): Row

    /**
     * Adds specified columns in a row
     */
    fun twoColumnsRow(column1: (Row.() -> Unit)?, column2: (Row.() -> Unit)? = null): Row

    /**
     * Adds specified columns in a row
     */
    fun threeColumnsRow(column1: (Row.() -> Unit)?, column2: (Row.() -> Unit)? = null, column3: (Row.() -> Unit)? = null): Row

    /**
     * Adds horizontal line separator with optional [title]
     */
    fun separator(@NlsContexts.Separator title: String? = null, background: Color? = null): Row

    /**
     * Creates sub-panel that occupies the whole width and uses its own grid inside
     */
    fun panel(init: Panel.() -> Unit): Panel

    /**
     * Registers [callback] that will be called from [DialogPanel.apply] method
     */
    fun onApply(callback: () -> Unit): Panel

    /**
     * Registers [callback] that will be called from [DialogPanel.reset] method
     */
    fun onReset(callback: () -> Unit): Panel

    /**
     * Registers [callback] that will be called from [DialogPanel.isModified] method
     */
    fun onIsModified(callback: () -> Boolean): Panel

    /**
     * Overrides default spacing configuration. Should be used for very specific cases
     */
    fun customizeSpacingConfiguration(spacingConfiguration: SpacingConfiguration, init: Panel.() -> Unit)

}