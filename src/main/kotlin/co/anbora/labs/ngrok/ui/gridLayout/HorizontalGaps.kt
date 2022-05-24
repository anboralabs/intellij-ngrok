package co.anbora.labs.ngrok.ui.gridLayout

import co.anbora.labs.ngrok.ui.checkNonNegative

data class HorizontalGaps(val left: Int = 0, val right: Int = 0) {
    companion object {
        @JvmField
        val EMPTY = HorizontalGaps()
    }

    init {
        checkNonNegative("left", left)
        checkNonNegative("right", right)
    }

    val width: Int
        get() = left + right
}