package co.anbora.labs.ngrok.settings

const val PORT_80 = 80
const val PORT_8000 = 8000
const val PORT_8080 = 8080

data class SettingOptionSet(
    var apiToken: String = "",
    var ports: MutableSet<Int> = mutableSetOf(PORT_80, PORT_8000, PORT_8080)
) {

    fun merge(configuredOptions: SettingOptionSet) {
        this.apiToken = configuredOptions.apiToken
        this.ports.addAll(configuredOptions.ports)
    }
}