package co.anbora.labs.ngrok.ui.gridLayout

interface Grid {

    /**
     * Set of columns that fill available extra space in container
     */
    val resizableColumns: MutableSet<Int>

    /**
     * Set of rows that fill available extra space in container
     */
    val resizableRows: MutableSet<Int>

    /**
     * Gaps around columns. Used only when column is visible
     */
    val columnsGaps: MutableList<HorizontalGaps>

    /**
     * Gaps around rows. Used only when row is visible
     */
    val rowsGaps: MutableList<VerticalGaps>
}