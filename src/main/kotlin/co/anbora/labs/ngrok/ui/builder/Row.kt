package co.anbora.labs.ngrok.ui.builder

import co.anbora.labs.ngrok.ui.LayoutDslMarker
import co.anbora.labs.ngrok.ui.builder.impl.MAX_LINE_LENGTH_WORD_WRAP
import co.anbora.labs.ngrok.ui.gridLayout.VerticalGaps
import com.intellij.icons.AllIcons
import com.intellij.ide.TooltipTitle
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.*
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.layout.ComponentPredicate
import com.intellij.util.Function
import com.intellij.util.execution.ParametersListUtil
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.NonNls
import java.awt.event.ActionEvent
import javax.swing.*

@LayoutDslMarker
interface Row {

    /**
     * Layout of the row.
     * Default value is [RowLayout.LABEL_ALIGNED] when label is provided for the row, [RowLayout.INDEPENDENT] otherwise
     */
    fun layout(rowLayout: RowLayout): Row

    /**
     * Marks the row as resizable: the row occupies all extra vertical space in parent (for example in [Panel.group] or [Panel.panel])
     * and changes size together with parent. When resizable is needed in whole [DialogPanel] all row parents should be marked
     * as [resizableRow] as well. It's possible to have several resizable rows, which means extra space is shared between them.
     * Note that vertical size and placement of components in the row are managed by [Cell.verticalAlign]
     *
     * @see [Grid.resizableRows]
     */
    fun resizableRow(): Row

    fun <T : JComponent> cell(component: T, viewComponent: JComponent = component): Cell<T>

    /**
     * Adds [component]. Use this method only for custom specific components, all standard components like label, button,
     * checkbox etc are covered by dedicated [Row] factory methods
     */
    fun <T : JComponent> cell(component: T): Cell<T>

    /**
     * Adds an empty cell in the grid
     */
    fun cell()

    /**
     * Adds [component] wrapped by [JBScrollPane]
     */
    fun <T : JComponent> scrollCell(component: T): Cell<T>

    /**
     * Sets visibility of the row including comment [Row.rowComment] and all children recursively.
     * The row is invisible if there is an invisible parent
     */
    fun visible(isVisible: Boolean): Row

    /**
     * Binds row visibility to provided [predicate]
     */
    fun visibleIf(predicate: ComponentPredicate): Row

    /**
     * Sets enabled state of the row including comment [Row.rowComment] and all children recursively.
     * The row is disabled if there is a disabled parent
     */
    fun enabled(isEnabled: Boolean): Row

    /**
     * Binds row enabled state to provided [predicate]
     */
    fun enabledIf(predicate: ComponentPredicate): Row

    /**
     * Adds additional gap above current row. It is visible together with the row.
     * Only greatest gap of top and bottom gaps is used between two rows (or top gap if equal)
     */
    fun topGap(topGap: TopGap): Row

    /**
     * Adds additional gap below current row. It is visible together with the row.
     * Only greatest gap of top and bottom gaps is used between two rows (or top gap if equal)
     */
    fun bottomGap(bottomGap: BottomGap): Row

    /**
     * Creates sub-panel inside the cell of the row. The panel contains its own rows and cells
     */
    fun panel(init: Panel.() -> Unit): Panel

    fun checkBox(@NlsContexts.Checkbox text: String): Cell<JBCheckBox>

    fun button(@NlsContexts.Button text: String, actionListener: (event: ActionEvent) -> Unit): Cell<JButton>

    fun button(@NlsContexts.Button text: String, action: AnAction, @NonNls actionPlace: String = ActionPlaces.UNKNOWN): Cell<JButton>

    fun actionButton(action: AnAction, @NonNls actionPlace: String = ActionPlaces.UNKNOWN): Cell<ActionButton>

    /**
     * Creates an [ActionButton] with [icon] and menu with provided [actions]
     */
    fun actionsButton(vararg actions: AnAction,
                      @NonNls actionPlace: String = ActionPlaces.UNKNOWN,
                      icon: Icon = AllIcons.General.GearPlain): Cell<ActionButton>

    fun slider(min: Int, max: Int, minorTickSpacing: Int, majorTickSpacing: Int): Cell<JSlider>

    /**
     * Adds a label. For label that relates to joined control [Panel.row] and [Cell.label] must be used,
     * because they set correct gap between label and component and set [JLabel.labelFor] property
     */
    fun label(@NlsContexts.Label text: String): Cell<JLabel>

    /**
     * Adds text. [text] can contain HTML tags except &lt;html&gt;, which is added automatically.
     * \n does not work as new line in html, use &lt;br&gt; instead.
     * Links with href to http/https are automatically marked with additional arrow icon.
     * It is preferable to use [label] method for short plain single-lined strings because labels use less resources and simpler
     *
     * @see DEFAULT_COMMENT_WIDTH
     * @see MAX_LINE_LENGTH_WORD_WRAP
     * @see MAX_LINE_LENGTH_NO_WRAP
     */
    fun text(@NlsContexts.Label text: String, maxLineLength: Int = MAX_LINE_LENGTH_WORD_WRAP,
             action: HyperlinkEventAction = HyperlinkEventAction.HTML_HYPERLINK_INSTANCE): Cell<JEditorPane>

    /**
     * Creates focusable link with text inside. Should not be used with html in [text]
     */
    fun link(@NlsContexts.LinkLabel text: String, action: (ActionEvent) -> Unit): Cell<ActionLink>

    /**
     * Creates focusable browser link with text inside. Should not be used with html in [text]
     */
    fun browserLink(@NlsContexts.LinkLabel text: String, url: String): Cell<BrowserLink>

    /**
     * @param item current item
     * @param items list of all available items in popup
     * @param onSelected invoked when item is selected
     * @param updateText true if after selection link text is updated, false otherwise
     */
    fun <T> dropDownLink(item: T, items: List<T>, onSelected: ((T) -> Unit)? = null, updateText: Boolean = true): Cell<DropDownLink<T>>

    fun icon(icon: Icon): Cell<JLabel>

    fun contextHelp(@NlsContexts.Tooltip description: String, @TooltipTitle title: String? = null): Cell<JLabel>

    /**
     * Creates text field with [columns] set to [COLUMNS_SHORT]
     */
    fun textField(): Cell<JBTextField>

    /**
     * Creates expandable text field with [columns] set to [COLUMNS_SHORT]
     */
    fun expandableTextField(parser: Function<in String, out MutableList<String>> = ParametersListUtil.DEFAULT_LINE_PARSER,
                            joiner: Function<in MutableList<String>, String> = ParametersListUtil.DEFAULT_LINE_JOINER): Cell<ExpandableTextField>

    /**
     * Creates integer text field with [columns] set to [COLUMNS_TINY]
     *
     * @param range allowed values range inclusive
     * @param keyboardStep increase/decrease step for keyboard keys up/down. The keys are not used if [keyboardStep] is null
     */
    fun intTextField(range: IntRange? = null, keyboardStep: Int? = null): Cell<JBTextField>

    /**
     * Creates spinner for int values
     *
     * @param range allowed values range inclusive
     */
    fun spinner(range: IntRange, step: Int = 1): Cell<JBIntSpinner>

    /**
     * Creates spinner for double values
     *
     * @param range allowed values range inclusive
     */
    fun spinner(range: ClosedRange<Double>, step: Double = 1.0): Cell<JSpinner>

    /**
     * Creates text area with [columns] set to [COLUMNS_SHORT]
     */
    fun textArea(): Cell<JBTextArea>

    fun <T> comboBox(model: ComboBoxModel<T>, renderer: ListCellRenderer<in T?>? = null): Cell<ComboBox<T>>

    fun <T> comboBox(items: Collection<T>, renderer: ListCellRenderer<in T?>? = null): Cell<ComboBox<T>>

    /**
     * Overrides all gaps around row by [customRowGaps]. Should be used for very specific cases
     */
    fun customize(customRowGaps: VerticalGaps): Row
}