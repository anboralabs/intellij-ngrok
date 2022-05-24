package co.anbora.labs.ngrok.ui.builder

import javax.swing.JCheckBox

enum class RowLayout {
    /**
     * All cells of the row (including label if present) independent of parent grid.
     * That means this row has its own grid
     */
    INDEPENDENT,

    /**
     * Label is aligned, other components independent of parent grid. If label is not provided
     * then first cell (sometimes can be [JCheckBox] for example) is considered as a label.
     * That means label is in parent grid, other components have own grid
     */
    LABEL_ALIGNED,

    /**
     * All components including label are in parent grid
     * That means label and other components are in parent grid
     */
    PARENT_GRID
}