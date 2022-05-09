package co.anbora.labs.ngrok.settings

import co.anbora.labs.ngrok.settings.StorageHelper.STATE_STORAGE_FILE
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "NgrokEditorSettings",
    storages = [Storage(STATE_STORAGE_FILE)]
)
class NgrokSettings: PersistentStateComponent<SettingOptionSet> {

    private val currentOptions: SettingOptionSet by lazy {
        SettingOptionSet()
    }

    override fun getState(): SettingOptionSet = currentOptions

    override fun loadState(state: SettingOptionSet) {
        currentOptions.merge(state)
    }

    var apiToken: String
        get() = currentOptions.apiToken
        set(value) { currentOptions.apiToken = value }

    var ports: MutableSet<Int>
        get() = currentOptions.ports
        set(value) { currentOptions.ports = value }

    companion object {
        @JvmStatic
        fun getInstance(): NgrokSettings {
            val application = ApplicationManager.getApplication()
            return if (application.isDisposed) NgrokSettings() else application.getService(
                NgrokSettings::class.java
            )
        }
    }
}