package co.anbora.labs.ngrok.ui.builder

import com.intellij.ui.dsl.builder.RowsRange
import com.intellij.ui.layout.ComponentPredicate

interface RowsRange {

    fun visible(isVisible: Boolean): RowsRange

    fun visibleIf(predicate: ComponentPredicate): RowsRange

    fun enabled(isEnabled: Boolean): RowsRange

    fun enabledIf(predicate: ComponentPredicate): RowsRange
}