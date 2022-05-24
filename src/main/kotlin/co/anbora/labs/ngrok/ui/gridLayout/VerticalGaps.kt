package co.anbora.labs.ngrok.ui.gridLayout

import co.anbora.labs.ngrok.ui.checkNonNegative

data class VerticalGaps(val top: Int = 0, val bottom: Int = 0) {
    companion object {
        @JvmField
        val EMPTY = VerticalGaps()
    }

    init {
        checkNonNegative("top", top)
        checkNonNegative("bottom", bottom)
    }

    val height: Int
        get() = top + bottom
}