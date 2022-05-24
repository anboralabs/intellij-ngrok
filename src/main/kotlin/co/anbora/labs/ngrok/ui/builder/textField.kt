package co.anbora.labs.ngrok.ui.builder

import com.intellij.ui.layout.GrowPolicy
import javax.swing.JTextField
import javax.swing.text.JTextComponent

/**
 * Columns for text components with tiny width. Used for [Row.intTextField] by default
 */
const val COLUMNS_TINY = 6

/**
 * Columns for text components with short width (used instead of deprecated [GrowPolicy.SHORT_TEXT])
 */
const val COLUMNS_SHORT = 18

/**
 * Columns for text components with medium width (used instead of deprecated [GrowPolicy.MEDIUM_TEXT])
 */
const val COLUMNS_MEDIUM = 25

const val COLUMNS_LARGE = 36

fun <T : JTextComponent> Cell<T>.text(text: String): Cell<T> {
    component.text = text
    return this
}

fun <T : JTextField> Cell<T>.columns(columns: Int) = apply {
    component.columns(columns)
}

fun <T : JTextField> T.columns(columns: Int) = apply {
    this.columns = columns
}