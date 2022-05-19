package co.anbora.labs.ngrok.remote.server.components.tabs

import co.anbora.labs.ngrok.remote.server.components.ValuesTable
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent

class PropertiesTab(properties: MutableMap<String, String?>) {
    companion object {
        const val TITLE = "Properties"
    }

    val component: JComponent
    private val table: ValuesTable

    init {
        table = ValuesTable(properties)
        component = JBScrollPane(table)
    }

    fun update(properties: MutableMap<String, String?>) {
        table.update(properties)
    }
}