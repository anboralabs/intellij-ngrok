package co.anbora.labs.ngrok.ui.builder

import com.intellij.ide.BrowserUtil
import javax.swing.event.HyperlinkEvent

fun interface HyperlinkEventAction {

    companion object {
        /**
         * Opens URL in a browser
         */
        @JvmField
        val HTML_HYPERLINK_INSTANCE = HyperlinkEventAction { e -> BrowserUtil.browse(e.url) }
    }

    fun hyperlinkActivated(e: HyperlinkEvent)

    @JvmDefault
    fun hyperlinkEntered(e: HyperlinkEvent) {
    }

    @JvmDefault
    fun hyperlinkExited(e: HyperlinkEvent) {
    }
}