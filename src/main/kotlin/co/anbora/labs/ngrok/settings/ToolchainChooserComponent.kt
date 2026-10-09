package co.anbora.labs.ngrok.settings

import co.anbora.labs.ngrok.icons.NgrokIcons
import co.anbora.labs.ngrok.toolchain.NgrokKnownToolchainsState
import com.intellij.icons.AllIcons
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.asContextElement
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.awt.event.ActionListener
import javax.swing.JList

class ToolchainChooserComponent(browseActionListener: ActionListener, onSelectAction: (ToolchainInfo) -> Unit) :
    ComponentWithBrowseButton<ComboBox<ToolchainInfo>>(ComboBox<ToolchainInfo>(), browseActionListener) {

    private val comboBox = childComponent
    private val knownToolchains get() = NgrokKnownToolchainsState.getInstance().knownToolchains
    private var knownToolchainInfos = emptyList<ToolchainInfo>()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.EDT + ModalityState.any().asContextElement())
    private var loadJob: Job? = null

    // Location requested through [select], applied once the toolchains are loaded
    private var locationToSelect: String? = null

    // Ignores the selection events fired by the combobox while its items are replaced
    private var isFillingItems = false

    class NoToolchain : ToolchainInfo("", "") {
        companion object {
            val instance = NoToolchain()
        }
    }

    init {
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
            if (isFillingItems) return@addItemListener
            val item = comboBox.selectedItem as? ToolchainInfo ?: return@addItemListener
            onSelectAction(item)
        }

        setButtonIcon(AllIcons.General.Add)

        refresh()
    }

    fun selectedToolchain(): ToolchainInfo? {
        return comboBox.selectedItem as? ToolchainInfo
    }

    /**
     * Reloads the known toolchains, obtaining their versions on the IO dispatcher, then fills the combobox on the EDT.
     */
    fun refresh() {
        val locations = knownToolchains.toList()

        loadJob?.cancel()
        loadJob = scope.launch {
            val infos = locations
                .map { location -> async { ToolchainInfo(location, NgrokConfigurationUtil.guessToolchainVersionAsync(location)) } }
                .awaitAll()
                .filter { it.version != NgrokConfigurationUtil.UNDEFINED_VERSION }

            knownToolchainInfos = infos
            isFillingItems = true
            try {
                comboBox.removeAllItems()
                infos.forEach(comboBox::addItem)
                comboBox.addItem(NoToolchain.instance)
            } finally {
                isFillingItems = false
            }

            locationToSelect?.let { select(it) }
        }
    }

    fun select(location: String) {
        locationToSelect = location
        if (location.isEmpty()) {
            comboBox.selectedItem = NoToolchain.instance
            return
        }

        val infoToSelect = knownToolchainInfos.find { it.location == location } ?: return
        comboBox.selectedItem = infoToSelect
    }
}
