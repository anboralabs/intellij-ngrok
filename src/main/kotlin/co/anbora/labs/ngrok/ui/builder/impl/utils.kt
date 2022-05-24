package co.anbora.labs.ngrok.ui.builder.impl

import co.anbora.labs.ngrok.ui.UiDslException
import co.anbora.labs.ngrok.ui.builder.HyperlinkEventAction
import co.anbora.labs.ngrok.ui.builder.SpacingConfiguration
import co.anbora.labs.ngrok.ui.builder.components.DslLabel
import co.anbora.labs.ngrok.ui.builder.components.DslLabelType
import co.anbora.labs.ngrok.ui.gridLayout.Gaps
import co.anbora.labs.ngrok.ui.gridLayout.GridLayoutComponentProperty
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.TitledSeparator
import com.intellij.ui.ToolbarDecorator
import javax.swing.*
import javax.swing.text.JTextComponent

private const val FAIL_ON_WARN = false

const val DSL_INT_TEXT_RANGE_PROPERTY = "dsl.intText.range"

/**
 * Component properties for UI DSL
 */
enum class DslComponentProperty {
    /**
     * A mark that component is a label of a row, see [Panel.row]
     *
     * Value: [Boolean]
     */
    ROW_LABEL,

    /**
     * Custom visual paddings, which are used instead of [JComponent.getInsets]
     *
     * Value: [Gaps]
     */
    VISUAL_PADDINGS
}

/**
 * Text uses word wrap if there is no enough width
 */
const val MAX_LINE_LENGTH_WORD_WRAP = -1

/**
 * Text is not wrapped and uses only html markup like <br>
 */
const val MAX_LINE_LENGTH_NO_WRAP = Int.MAX_VALUE

private val LOG = Logger.getInstance("Jetbrains UI DSL")

/**
 * Components that can have assigned labels
 */
private val ALLOWED_LABEL_COMPONENTS = listOf(
    JComboBox::class,
    JSlider::class,
    JSpinner::class,
    JTable::class,
    JTextComponent::class,
    JTree::class
)

private val DEFAULT_VERTICAL_GAP_COMPONENTS = setOf(
    TextFieldWithBrowseButton::class,
    TitledSeparator::class
)

fun warn(message: String) {
    if (FAIL_ON_WARN) {
        throw UiDslException(message)
    }
    else {
        LOG.warn(message)
    }
}

fun createComment(@NlsContexts.Label text: String, maxLineLength: Int, action: HyperlinkEventAction): DslLabel {
    val result = DslLabel(DslLabelType.COMMENT)
    result.action = action
    result.maxLineLength = maxLineLength
    result.text = text
    return result
}

val JComponent.origin: JComponent
    get() {
        return when (this) {
            is TextFieldWithBrowseButton -> textField
            else -> this
        }
    }

fun prepareVisualPaddings(component: JComponent): Gaps {
    val insets = component.insets
    val customVisualPaddings = component.getClientProperty(DslComponentProperty.VISUAL_PADDINGS) as? Gaps

    if (customVisualPaddings == null) {
        return Gaps(
            top = insets.top,
            left = insets.left,
            bottom = insets.bottom,
            right = insets.right
        )
    }
    component.putClientProperty(GridLayoutComponentProperty.SUB_GRID_AUTO_VISUAL_PADDINGS, false)
    return customVisualPaddings
}

fun isAllowedLabel(cell: CellBaseImpl<*>?): Boolean {
    return cell is CellImpl<*> && ALLOWED_LABEL_COMPONENTS.any { clazz -> clazz.isInstance(cell.component.origin) }
}

fun labelCell(label: JLabel, cell: CellBaseImpl<*>?) {
    if (isAllowedLabel(cell)) {
        label.labelFor = (cell as CellImpl<*>).component.origin
    }
}

/**
 * Returns default top and bottom gap for [component]. All non [JPanel] components or
 * [DEFAULT_VERTICAL_GAP_COMPONENTS] have default vertical gap, zero otherwise
 */
fun getDefaultVerticalGap(component: JComponent, spacing: SpacingConfiguration): Int {
    val noDefaultVerticalGap = component is JPanel
            && component.getClientProperty(ToolbarDecorator.DECORATOR_KEY) == null
            && !DEFAULT_VERTICAL_GAP_COMPONENTS.any { clazz -> clazz.isInstance(component) }

    return if (noDefaultVerticalGap) 0 else spacing.verticalComponentGap
}

fun getComponentGaps(left: Int, right: Int, component: JComponent, spacing: SpacingConfiguration): Gaps {
    val top = getDefaultVerticalGap(component, spacing)
    var bottom = top
    if (component is JLabel && component.getClientProperty(DslComponentPropertyInternal.LABEL_NO_BOTTOM_GAP) == true) {
        bottom = 0
    }
    return Gaps(top = top, left = left, bottom = bottom, right = right)
}