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
     * Adds standard left indent and groups rows into [RowsRange] that allows to use some groups operations on the rows
     *
     * @see [rowsRange]
     */
    fun indent(init: Panel.() -> Unit): RowsRange

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
     * Groups rows into [RowsRange] that allows to use some groups operations on the rows
     *
     * @see [indent]
     */
    fun rowsRange(init: Panel.() -> Unit): RowsRange

    /**
     * Adds panel with independent grid, title and some vertical space above (except the group in the parents first row)
     * and below (except the group in the parents last row) the group.
     * Grouped radio buttons and checkboxes should use [Panel.buttonsGroup] method, which uses different title gaps.
     * To change gaps around the group use [Row.topGap] and [Row.bottomGap] for the method result
     *
     * @param indent true if left indent is needed
     */
    fun group(@NlsContexts.BorderTitle title: String? = null,
              indent: Boolean = true,
              init: Panel.() -> Unit): Row

    @Deprecated("Use overloaded group(...) instead")
    @ApiStatus.ScheduledForRemoval
    fun group(@NlsContexts.BorderTitle title: String? = null,
              indent: Boolean = true,
              topGroupGap: Boolean? = null,
              bottomGroupGap: Boolean? = null,
              init: Panel.() -> Unit): Panel

    /**
     * Similar to [Panel.group] but uses the same grid as the parent.
     *
     * @see [RowsRange]
     */
    fun groupRowsRange(@NlsContexts.BorderTitle title: String? = null,
                       indent: Boolean = true,
                       topGroupGap: Boolean? = null,
                       bottomGroupGap: Boolean? = null,
                       init: Panel.() -> Unit): RowsRange

    /**
     * Adds collapsible panel with independent grid, title and some vertical space above (except the group in the parents first row)
     * and below (except the group in the parents last row) the group.
     * To change gaps around the group use [Row.topGap] and [Row.bottomGap] for the method result
     *
     * @param indent true if left indent is needed
     */
    fun collapsibleGroup(@NlsContexts.BorderTitle title: String,
                         indent: Boolean = true,
                         init: Panel.() -> Unit): CollapsibleRow

    /**
     * Unions [Row.radioButton] in one group. Must be also used for [Row.checkBox] if they are grouped with some title.
     * Note that [Panel.group] provides different gaps around the title

     * @param indent true if left indent is needed. By default, true if title exists and false otherwise
     */
    fun buttonsGroup(@NlsContexts.BorderTitle title: String? = null, indent: Boolean = title != null, init: Panel.() -> Unit): ButtonsGroup

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