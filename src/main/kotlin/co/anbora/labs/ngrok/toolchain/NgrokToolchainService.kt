package co.anbora.labs.ngrok.toolchain

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute

@State(
    name = "Ngrok Toolchain",
    storages = [Storage("NewNgrokHome.xml")]
)
class NgrokToolchainService: PersistentStateComponent<NgrokToolchainService.ToolchainState?> {
    private var state = ToolchainState()
    val toolchainLocation: String
        get() = state.toolchainLocation

    @Volatile
    private var toolchain: NgrokToolchain = NgrokToolchain.NULL

    fun setToolchain(newToolchain: NgrokToolchain) {
        toolchain = newToolchain
        state.toolchainLocation = newToolchain.homePath()
    }

    fun toolchain(): NgrokToolchain {
        val currentLocation = state.toolchainLocation
        if (toolchain == NgrokToolchain.NULL && currentLocation.isNotEmpty()) {
            setToolchain(NgrokToolchain.fromPath(currentLocation))
        }
        return toolchain
    }

    fun isNotSet(): Boolean = toolchain == NgrokToolchain.NULL

    override fun getState() = state

    override fun loadState(state: ToolchainState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    companion object {
        val toolchainSettings
            get() = service<NgrokToolchainService>()
    }

    class ToolchainState {
        @Attribute("url")
        var toolchainLocation: String = ""
    }
}
