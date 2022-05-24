package co.anbora.labs.ngrok.ui.builder.impl

import co.anbora.labs.ngrok.ui.UiDslException
import co.anbora.labs.ngrok.ui.builder.HyperlinkEventAction
import co.anbora.labs.ngrok.ui.builder.components.DslLabel
import co.anbora.labs.ngrok.ui.builder.components.DslLabelType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.NlsContexts
import javax.swing.JComponent

private const val FAIL_ON_WARN = false

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
 * Text is not wrapped and uses only html markup like <br>
 */
const val MAX_LINE_LENGTH_NO_WRAP = Int.MAX_VALUE

private val LOG = Logger.getInstance("Jetbrains UI DSL")

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