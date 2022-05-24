package co.anbora.labs.ngrok.ui.gridLayout

import co.anbora.labs.ngrok.ui.checkNonNegative
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBEmptyBorder

data class Gaps(val top: Int = 0, val left: Int = 0, val bottom: Int = 0, val right: Int = 0) {
    companion object {
        @JvmField
        val EMPTY = Gaps(0)
    }

    init {
        checkNonNegative("top", top)
        checkNonNegative("left", left)
        checkNonNegative("bottom", bottom)
        checkNonNegative("right", right)
    }

    constructor(size: Int) : this(size, size, size, size)

    val width: Int
        get() = left + right

    val height: Int
        get() = top + bottom
}

fun JBGaps(top: Int = 0, left: Int = 0, bottom: Int = 0, right: Int = 0): Gaps {
    return Gaps(JBUIScale.scale(top), JBUIScale.scale(left), JBUIScale.scale(bottom), JBUIScale.scale(right))
}

fun Gaps.toJBEmptyBorder(): JBEmptyBorder {
    return JBEmptyBorder(top, left, bottom, right)
}