package co.anbora.labs.ngrok.toolchain

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "Ngrok Home",
    storages = [Storage("NewNgrokToolchains.xml")]
)
class NgrokKnownToolchainsState : PersistentStateComponent<NgrokKnownToolchainsState?> {
    companion object {
        fun getInstance() = service<NgrokKnownToolchainsState>()
    }

    var knownToolchains: Set<String> = emptySet()

    fun isKnown(homePath: String): Boolean {
        return knownToolchains.contains(homePath)
    }

    fun add(toolchain: NgrokToolchain) {
        knownToolchains = knownToolchains + toolchain.homePath()
    }

    override fun getState() = this

    override fun loadState(state: NgrokKnownToolchainsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
