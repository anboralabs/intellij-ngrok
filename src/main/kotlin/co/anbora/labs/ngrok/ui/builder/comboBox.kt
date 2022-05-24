package co.anbora.labs.ngrok.ui.builder

import com.intellij.openapi.ui.ComboBox

/**
 * Minimal width of combobox in chars
 *
 * @see COLUMNS_TINY
 * @see COLUMNS_SHORT
 * @see COLUMNS_MEDIUM
 * @see COLUMNS_LARGE
 */
fun <T, C : ComboBox<T>> Cell<C>.columns(columns: Int) = apply {
    component.columns(columns)
}

fun <T, C : ComboBox<T>> C.columns(columns: Int) = apply {
    // See JTextField.getColumnWidth implementation
    val columnWidth = getFontMetrics(font).charWidth('m')
    setMinimumAndPreferredWidth(columns * columnWidth + insets.left + insets.right)
}