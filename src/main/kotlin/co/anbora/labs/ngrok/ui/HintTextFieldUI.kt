package co.anbora.labs.ngrok.ui

import java.awt.Graphics
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.plaf.basic.BasicTextFieldUI

class HintTextFieldUI: BasicTextFieldUI(), FocusListener {

    override fun paintSafely(g: Graphics?) {
        super.paintSafely(g)
    }

    override fun focusGained(e: FocusEvent?) {
        TODO("Not yet implemented")
    }

    override fun focusLost(e: FocusEvent?) {
        TODO("Not yet implemented")
    }
}
