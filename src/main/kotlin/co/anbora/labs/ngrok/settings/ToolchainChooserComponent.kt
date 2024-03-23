package co.anbora.labs.ngrok.settings

import co.anbora.labs.ngrok.icons.NgrokIcons
import co.anbora.labs.ngrok.toolchain.NgrokKnownToolchainsState
import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import java.awt.event.ActionListener
import javax.swing.JList

class ToolchainChooserComponent(browseActionListener: ActionListener, onSelectAction: (ToolchainInfo) -> Unit) :
    ComponentWithBrowseButton<ComboBox<ToolchainInfo>>(ComboBox<ToolchainInfo>(), browseActionListener) {

    private val comboBox = childComponent
    private val knownToolchains get() = NgrokKnownToolchainsState.getInstance().knownToolchains
    private var knownToolchainInfos = knownToolchains
        .map { ToolchainInfo(it, NgrokConfigurationUtil.guessToolchainVersion(it)) }
        .filter { it.version != NgrokConfigurationUtil.UNDEFINED_VERSION }

    class NoToolchain : ToolchainInfo("", "") {
        companion object {
            val instance = NoToolchain()
        }
    }

    init {
        knownToolchainInfos.forEach { info ->
            comboBox.addItem(info)
        }

        comboBox.addItem(NoToolchain.instance)

        comboBox.renderer = object : ColoredListCellRenderer<ToolchainInfo>() {
            override fun customizeCellRenderer(
                list: JList<out ToolchainInfo>,
                value: ToolchainInfo?,
                index: Int,
                selected: Boolean,
                hasFocus: Boolean,
            ) {
                if (value == null || value is NoToolchain) {
                    append("<No Ngrok>")
                    return
                }

                icon = NgrokIcons.NGROK
                append(value.version)
                append("  ")
                append(value.location, SimpleTextAttributes.GRAYED_ATTRIBUTES)
            }
        }

        comboBox.addItemListener {
            val item = comboBox.selectedItem as? ToolchainInfo ?: return@addItemListener
            onSelectAction(item)
        }

        setButtonIcon(AllIcons.General.Add)
    }

    fun selectedToolchain(): ToolchainInfo? {
        return comboBox.selectedItem as? ToolchainInfo
    }

    fun refresh() {
        comboBox.removeAllItems()
        knownToolchainInfos = knownToolchains
            .map { ToolchainInfo(it, NgrokConfigurationUtil.guessToolchainVersion(it)) }
            .filter { it.version != NgrokConfigurationUtil.UNDEFINED_VERSION }

        knownToolchainInfos.forEach { info ->
            comboBox.addItem(info)
        }
        comboBox.addItem(NoToolchain())
    }

    fun select(location: String) {
        if (location.isEmpty()) {
            comboBox.selectedItem = NoToolchain.instance
            return
        }

        val infoToSelect = knownToolchainInfos.find { it.location == location } ?: return
        comboBox.selectedItem = infoToSelect
    }
}
