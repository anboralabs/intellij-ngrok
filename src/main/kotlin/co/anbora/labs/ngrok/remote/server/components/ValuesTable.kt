package co.anbora.labs.ngrok.remote.server.components

import com.intellij.ui.table.JBTable
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import javax.swing.JComponent
import javax.swing.KeyStroke
import javax.swing.table.DefaultTableModel


class ValuesTable(values: MutableMap<String, String?>) : JBTable(), ActionListener {
    companion object {
        private const val NAME_COLUMN_TITLE = "Name"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    private val keyRowMap: MutableMap<String, Int> = mutableMapOf()
    private val tableModel: DefaultTableModel = DefaultTableModel(arrayOf(NAME_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

    private val copy: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false)
    private val copyMac: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.META_MASK, false)

    init {
        var index = 0
        for (value in values) {
            tableModel.addRow(arrayOf(value.key, value.value))
            keyRowMap[value.key] = index
            index++
        }

        model = tableModel

        this.registerKeyboardAction(this, copy, JComponent.WHEN_FOCUSED)
        this.registerKeyboardAction(this, copyMac, JComponent.WHEN_FOCUSED)
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false

    fun update(values: MutableMap<String, String?>) {
        val deletedRows = keyRowMap.keys.subtract(values.keys)

        for (value in values) {
            if (keyRowMap.containsKey(value.key)) {
                val row = keyRowMap[value.key]!!
                val currentValue = tableModel.getValueAt(row, 1)
                if (currentValue != value.value) {
                    tableModel.setValueAt(value.value, row, 1)
                }
            } else {
                tableModel.addRow(arrayOf(value.key, value.value))
                val index = (keyRowMap.values.maxOrNull() ?: -1) + 1
                keyRowMap[value.key] = index
            }
        }

        for (deletedRow in deletedRows.sortedByDescending { it }) {
            val row = keyRowMap[deletedRow]!!
            tableModel.removeRow(row)
            keyRowMap.remove(deletedRow)
        }
    }

    override fun actionPerformed(e: ActionEvent?) {
        val sbf = selectedRows.map { this.getValueAt(it, 1) }.joinToString()
        val selection = StringSelection(sbf)
        val clipBoard = Toolkit.getDefaultToolkit().systemClipboard
        clipBoard.setContents(selection, selection)
    }
}