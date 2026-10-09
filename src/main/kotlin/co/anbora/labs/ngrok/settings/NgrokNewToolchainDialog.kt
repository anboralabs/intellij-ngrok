package co.anbora.labs.ngrok.settings

import co.anbora.labs.ngrok.toolchain.NgrokKnownToolchainsState
import co.anbora.labs.ngrok.toolchain.NgrokToolchain
import co.anbora.labs.ngrok.toolchain.NgrokToolchainFlavor
import co.anbora.labs.ngrok.utils.toPath
import co.anbora.labs.ngrok.utils.toPathOrNull
import com.intellij.icons.AllIcons
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.asContextElement
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Disposer
import com.intellij.ui.AnimatedIcon
import com.intellij.ui.JBColor
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.util.ui.JBDimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.file.Path
import javax.swing.JComponent
import javax.swing.JLabel
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.time.Duration.Companion.milliseconds

class NgrokNewToolchainDialog(private val toolchainFilter: Condition<Path>, project: Project?) : DialogWrapper(project) {
    data class Model(
        var toolchainLocation: String,
        var toolchainVersion: String,
        var stdlibLocation: String,
    )

    private val model: Model = Model("", "N/A", "")
    private val mainPanel: DialogPanel
    private val toolchainVersion = JLabel()
    private val toolchainIconLabel = JLabel()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.EDT + ModalityState.any().asContextElement())
    private var versionJob: Job? = null

    private val pathToToolchainComboBox = NgrokToolchainPathChoosingComboBox { onToolchainLocationChanged() }

    init {
        title = "New Ngrok"

        Disposer.register(this.disposable) { scope.cancel() }

        setOKButtonText("Add")

        mainPanel = panel {
            row("Location:") {
                cell(pathToToolchainComboBox)
                    .align(AlignX.FILL)
                    .validationOnApply { validateToolchainPath() }
            }
            row("Version:") {
                cell(toolchainVersion)
                    .bind(JLabel::getText, JLabel::setText, model::toolchainVersion.toMutableProperty())
                    .gap(RightGap.SMALL)
                    .apply {
                        component.foreground = JBColor.RED
                    }
                cell(toolchainIconLabel)
            }
            row("Binary:") {
                textField()
                    .align(AlignX.FILL)
                    .bindText(model::stdlibLocation)
                    .enabled(false)
            }
        }

        pathToToolchainComboBox.addToolchainsAsync(this.disposable) {
            NgrokToolchainFlavor.getApplicableFlavors().flatMap { it.suggestHomePaths() }.distinct().
                    filter { toolchainFilter.value(it) }
        }

        val disposable = Disposer.newDisposable()
        mainPanel.registerValidators(disposable)

        init()
    }

    override fun getPreferredFocusedComponent(): JComponent = pathToToolchainComboBox

    override fun createCenterPanel(): JComponent {
        return mainPanel.apply {
            preferredSize = JBDimension(450, height)
        }
    }

    override fun doOKAction() {
        if (NgrokKnownToolchainsState.getInstance().isKnown(model.toolchainLocation)) {
            setErrorText("This location is already added")
            return
        }

        NgrokKnownToolchainsState.getInstance().add(NgrokToolchain.fromPath(model.toolchainLocation, model.toolchainVersion))

        super.doOKAction()
    }

    fun addedToolchain(): String? {
        return if (exitCode == OK_EXIT_CODE) model.toolchainLocation else null
    }

    private fun onToolchainLocationChanged() {
        model.toolchainLocation = pathToToolchainComboBox.selectedPath ?: ""
        val location = model.toolchainLocation

        // Until the version of the new location is known, the model keeps it as undefined
        updateToolchainVersion(NgrokConfigurationUtil.UNDEFINED_VERSION)

        versionJob?.cancel()
        if (location.isBlank()) {
            return
        }

        showDetectingVersion()
        versionJob = scope.launch {
            // Wait for the user to stop typing before running ngrok
            delay(300.milliseconds)
            updateToolchainVersion(NgrokConfigurationUtil.guessToolchainVersionAsync(location))
        }
    }

    private fun showDetectingVersion() {
        toolchainVersion.text = "Detecting..."
        toolchainVersion.foreground = JBColor.foreground()
        toolchainIconLabel.icon = AnimatedIcon.Default.INSTANCE
    }

    private fun updateToolchainVersion(version: String) {
        model.toolchainVersion = version

        if (model.toolchainVersion != NgrokConfigurationUtil.UNDEFINED_VERSION) {
            val path = model.toolchainLocation.toPathOrNull()
            model.stdlibLocation = path?.resolve(NgrokConfigurationUtil.STANDARD_V_COMPILER)?.toString() ?: ""
        } else {
            model.stdlibLocation = ""
        }

        mainPanel.reset()

        if (model.toolchainVersion == NgrokConfigurationUtil.UNDEFINED_VERSION) {
            toolchainVersion.foreground = JBColor.RED
            toolchainIconLabel.icon = null
        } else {
            toolchainVersion.foreground = JBColor.foreground()
            toolchainIconLabel.icon = AllIcons.General.InspectionsOK
        }
    }

    private fun ValidationInfoBuilder.validateToolchainPath(): ValidationInfo? {
        if (model.toolchainLocation.isEmpty()) {
            return error("Ngrok location is required")
        }

        val toolchainPath = model.toolchainLocation.toPath()
        if (!toolchainPath.exists()) {
            return error("Ngrok location is invalid, $toolchainPath not exist")
        }

        if (!toolchainPath.isDirectory()) {
            return error("Ngrok location must be a directory")
        }

        val version = model.toolchainVersion
        if (version == NgrokConfigurationUtil.UNDEFINED_VERSION) {
            return error("Ngrok location is invalid, can't get version. Please check that folder contains ${NgrokConfigurationUtil.STANDARD_V_COMPILER}")
        }

        return null
    }
}
