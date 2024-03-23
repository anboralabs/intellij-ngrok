package co.anbora.labs.ngrok.settings

import co.anbora.labs.ngrok.utils.addTextChangeListener
import co.anbora.labs.ngrok.utils.pathAsPath
import com.intellij.openapi.application.AppUIExecutor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.ComboBoxWithWidePopup
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.ui.AnimatedIcon
import com.intellij.ui.ComboboxSpeedSearch
import com.intellij.ui.components.fields.ExtendableTextComponent
import com.intellij.ui.components.fields.ExtendableTextField
import java.nio.file.Path
import javax.swing.plaf.basic.BasicComboBoxEditor
import kotlin.io.path.pathString

class NgrokToolchainPathChoosingComboBox(onTextChanged: () -> Unit = {}) :
    ComponentWithBrowseButton<ComboBoxWithWidePopup<Path>>(ComboBoxWithWidePopup(), null) {

    private val editor: BasicComboBoxEditor = object : BasicComboBoxEditor() {
        override fun createEditorComponent(): ExtendableTextField = ExtendableTextField()
    }

    private val pathTextField: ExtendableTextField
        get() = childComponent.editor.editorComponent as ExtendableTextField

    private val busyIconExtension: ExtendableTextComponent.Extension =
        ExtendableTextComponent.Extension { AnimatedIcon.Default.INSTANCE }

    var selectedPath: String?
        get() = pathTextField.text
        set(value) {
            pathTextField.text = value.orEmpty()
        }

    init {
        ComboboxSpeedSearch(childComponent)
        childComponent.editor = editor
        childComponent.isEditable = true

        addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            FileChooser.chooseFile(descriptor, null, null) { file ->
                childComponent.selectedItem = file.pathAsPath
            }
        }

        pathTextField.addTextChangeListener { onTextChanged() }
    }

    private fun setBusy(busy: Boolean) {
        if (busy) {
            pathTextField.addExtension(busyIconExtension)
        } else {
            pathTextField.removeExtension(busyIconExtension)
        }
        repaint()
    }

    /**
     * Obtains a list of toolchains on a pool using [toolchainObtainer], then fills the combobox and calls [callback] on the EDT.
     */
    @Suppress("UnstableApiUsage")
    fun addToolchainsAsync(toolchainObtainer: () -> List<Path>) {
        setBusy(true)
        ApplicationManager.getApplication().executeOnPooledThread {
            var toolchains = emptyList<Path>()
            try {
                toolchains = toolchainObtainer()
            } finally {
                val executor = AppUIExecutor.onUiThread(ModalityState.any()).expireWith(this)
                executor.execute {
                    setBusy(false)
                    childComponent.removeAllItems()
                    toolchains.forEach(childComponent::addItem)
                    selectedPath = selectedPath?.ifEmpty { null } ?: (toolchains.firstOrNull()?.pathString ?: "")
                }
            }
        }
    }
}
