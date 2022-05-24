package co.anbora.labs.ngrok.ui.builder.impl

import co.anbora.labs.ngrok.ui.UiDslException
import com.intellij.ui.dsl.gridLayout.*
import com.intellij.ui.dsl.gridLayout.impl.*
import java.awt.Dimension
import java.awt.Insets
import java.awt.Rectangle
import javax.swing.JComponent
import kotlin.math.max
import kotlin.math.min

class GridImpl : Grid {

    override val resizableColumns = mutableSetOf<Int>()
    override val resizableRows = mutableSetOf<Int>()

    override val columnsGaps = mutableListOf<HorizontalGaps>()
    override val rowsGaps = mutableListOf<VerticalGaps>()

    val visible: Boolean
        get() = cells.any { it.visible }

    private val layoutData = LayoutData()
    private val cells = mutableListOf<Cell>()

    fun register(component: JComponent, constraints: Constraints) {
        if (!isEmpty(constraints)) {
            throw UiDslException("Some cells are occupied already: $constraints")
        }

        cells.add(ComponentCell(constraints, component))
    }

    fun registerSubGrid(constraints: Constraints): Grid {
        if (!isEmpty(constraints)) {
            throw UiDslException("Some cells are occupied already: $constraints")
        }

        val result = com.intellij.ui.dsl.gridLayout.impl.GridImpl()
        cells.add(GridCell(constraints, result))
        return result
    }

    fun unregister(component: JComponent): Boolean {
        val iterator = cells.iterator()
        for (cell in iterator) {
            when (cell) {
                is ComponentCell -> {
                    if (cell.component == component) {
                        iterator.remove()
                        return true
                    }
                }
                is GridCell -> {
                    if (cell.content.unregister(component)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun getPreferredSizeData(parentInsets: Insets): PreferredSizeData {
        calculatePreferredLayoutData()

        val outsideGaps = layoutData.getOutsideGaps(parentInsets)
        return PreferredSizeData(
            Dimension(layoutData.preferredWidth + outsideGaps.width, layoutData.preferredHeight + outsideGaps.height),
            outsideGaps
        )
    }

    /**
     * Calculates [layoutData] and layouts all components
     */
    fun layout(width: Int, height: Int, parentInsets: Insets) {
        calculatePreferredLayoutData()
        val outsideGaps = layoutData.getOutsideGaps(parentInsets)

        // Recalculate LayoutData for requested size with corrected insets
        calculateLayoutDataStep2(width - outsideGaps.width)
        calculateLayoutDataStep3()
        calculateLayoutDataStep4(height - outsideGaps.height)

        layout(outsideGaps.left, outsideGaps.top)
    }

    /**
     * Layouts components
     */
    fun layout(x: Int, y: Int) {
        for (layoutCellData in layoutData.visibleCellsData) {
            val bounds = calculateBounds(layoutCellData, x, y)
            when (val cell = layoutCellData.cell) {
                is ComponentCell -> {
                    cell.component.bounds = bounds
                }
                is GridCell -> {
                    cell.content.layout(bounds.x, bounds.y)
                }
            }
        }
    }

    /**
     * Collects PreCalculationData for all components (including sub-grids) and applies size groups
     */
    private fun collectPreCalculationData(): Map<JComponent, PreCalculationData> {
        val result = mutableMapOf<JComponent, PreCalculationData>()
        collectPreCalculationData(result)

        val widthGroups = result.values.groupBy { it.constraints.widthGroup }
        for ((widthGroup, preCalculationDataList) in widthGroups) {
            if (widthGroup == null) {
                continue
            }
            val maxWidth = preCalculationDataList.maxOf { it.calculatedPreferredSize.width }
            for (preCalculationData in preCalculationDataList) {
                preCalculationData.calculatedPreferredSize.width = maxWidth
            }
        }

        return result
    }

    fun collectPreCalculationData(preCalculationDataMap: MutableMap<JComponent, PreCalculationData>) {
        for (cell in cells) {
            when (cell) {
                is ComponentCell -> {
                    val component = cell.component
                    if (!component.isVisible) {
                        continue
                    }
                    val componentMinimumSize = component.minimumSize
                    val componentPreferredSize = component.preferredSize
                    preCalculationDataMap[component] = PreCalculationData(componentMinimumSize, componentPreferredSize, cell.constraints)
                }

                is GridCell -> {
                    cell.content.collectPreCalculationData(preCalculationDataMap)
                }
            }
        }
    }

    /**
     * Calculates [layoutData] for preferred size
     */
    private fun calculatePreferredLayoutData() {
        val preCalculationDataMap = collectPreCalculationData()

        calculateLayoutDataStep1(preCalculationDataMap)
        calculateLayoutDataStep2(layoutData.preferredWidth)
        calculateLayoutDataStep3()
        calculateLayoutDataStep4(layoutData.preferredHeight)
        calculateOutsideGaps(layoutData.preferredWidth, layoutData.preferredHeight)
    }

    /**
     * Step 1 of [layoutData] calculations
     */
    fun calculateLayoutDataStep1(preCalculationDataMap: Map<JComponent, PreCalculationData>) {
        layoutData.columnsSizeCalculator.reset()
        val visibleCellsData = mutableListOf<LayoutCellData>()
        var columnsCount = 0
        var rowsCount = 0

        for (cell in cells) {
            val preferredSize: Dimension

            when (cell) {
                is ComponentCell -> {
                    val component = cell.component
                    if (!component.isVisible) {
                        continue
                    }
                    val preCalculationData = preCalculationDataMap[component]
                    preferredSize = preCalculationData!!.calculatedPreferredSize
                }

                is GridCell -> {
                    val grid = cell.content
                    if (!grid.visible) {
                        continue
                    }
                    grid.calculateLayoutDataStep1(preCalculationDataMap)
                    preferredSize = Dimension(grid.layoutData.preferredWidth, 0)
                }
            }

            val layoutCellData: LayoutCellData
            with(cell.constraints) {
                layoutCellData = LayoutCellData(cell = cell,
                    preferredSize = preferredSize,
                    columnGaps = HorizontalGaps(
                        left = columnsGaps.getOrNull(x)?.left ?: 0,
                        right = columnsGaps.getOrNull(x + width - 1)?.right ?: 0),
                    rowGaps = VerticalGaps(
                        top = rowsGaps.getOrNull(y)?.top ?: 0,
                        bottom = rowsGaps.getOrNull(y + height - 1)?.bottom ?: 0)
                )

                columnsCount = max(columnsCount, x + width)
                rowsCount = max(rowsCount, y + height)
            }

            visibleCellsData.add(layoutCellData)
            layoutData.columnsSizeCalculator.addConstraint(cell.constraints.x, cell.constraints.width, layoutCellData.cellPaddedWidth)
        }

        layoutData.visibleCellsData = visibleCellsData
        layoutData.preferredWidth = layoutData.columnsSizeCalculator.calculatePreferredSize()
        layoutData.dimension.setSize(columnsCount, rowsCount)
    }

    /**
     * Step 2 of [layoutData] calculations
     */
    fun calculateLayoutDataStep2(width: Int) {
        layoutData.columnsCoord = layoutData.columnsSizeCalculator.calculateCoords(width, resizableColumns)

        for (layoutCellData in layoutData.visibleCellsData) {
            val cell = layoutCellData.cell
            if (cell is GridCell) {
                cell.content.calculateLayoutDataStep2(layoutData.getFullPaddedWidth(layoutCellData))
            }
        }
    }

    /**
     * Step 3 of [layoutData] calculations
     */
    fun calculateLayoutDataStep3() {
        layoutData.rowsSizeCalculator.reset()
        layoutData.baselineData.reset()

        for (layoutCellData in layoutData.visibleCellsData) {
            val constraints = layoutCellData.cell.constraints
            layoutCellData.baseline = null
            when (val cell = layoutCellData.cell) {
                is ComponentCell -> {
                    if (!isSupportedBaseline(constraints)) {
                        continue
                    }

                    val componentWidth = layoutData.getPaddedWidth(layoutCellData) + constraints.visualPaddings.width
                    val baseline: Int
                    if (componentWidth >= 0) {
                        baseline = cell.constraints.componentHelper?.getBaseline(componentWidth, layoutCellData.preferredSize.height)
                            ?: cell.component.getBaseline(componentWidth, layoutCellData.preferredSize.height)
                        // getBaseline changes preferredSize, at least for JLabel
                        layoutCellData.preferredSize.height = cell.component.preferredSize.height
                    }
                    else {
                        baseline = -1
                    }

                    if (baseline >= 0) {
                        layoutCellData.baseline = baseline
                        layoutData.baselineData.registerBaseline(layoutCellData, baseline)
                    }
                }

                is GridCell -> {
                    val grid = cell.content
                    grid.calculateLayoutDataStep3()
                    layoutCellData.preferredSize.height = grid.layoutData.preferredHeight
                    if (grid.layoutData.dimension.height == 1 && isSupportedBaseline(constraints)) {
                        // Calculate baseline for grid
                        val gridBaselines = VerticalAlign.values()
                            .mapNotNull {
                                var result: Pair<VerticalAlign, RowBaselineData>? = null
                                if (it != VerticalAlign.FILL) {
                                    val baselineData = grid.layoutData.baselineData.get(it)
                                    if (baselineData != null) {
                                        result = Pair(it, baselineData)
                                    }
                                }
                                result
                            }

                        if (gridBaselines.size == 1) {
                            val (verticalAlign, gridBaselineData) = gridBaselines[0]
                            val baseline = calculateBaseline(layoutCellData.preferredSize.height, verticalAlign, gridBaselineData)
                            layoutCellData.baseline = baseline
                            layoutData.baselineData.registerBaseline(layoutCellData, baseline)
                        }
                    }
                }
            }
        }

        for (layoutCellData in layoutData.visibleCellsData) {
            val constraints = layoutCellData.cell.constraints
            val height = if (layoutCellData.baseline == null)
                layoutCellData.gapHeight - layoutCellData.cell.constraints.visualPaddings.height + layoutCellData.preferredSize.height
            else {
                val rowBaselineData = layoutData.baselineData.get(layoutCellData)
                rowBaselineData!!.height
            }

            // Cell height including gaps and excluding visualPaddings
            layoutData.rowsSizeCalculator.addConstraint(constraints.y, constraints.height, height)
        }

        layoutData.preferredHeight = layoutData.rowsSizeCalculator.calculatePreferredSize()
    }

    /**
     * Step 4 of [layoutData] calculations
     */
    fun calculateLayoutDataStep4(height: Int) {
        layoutData.rowsCoord = layoutData.rowsSizeCalculator.calculateCoords(height, resizableRows)

        for (layoutCellData in layoutData.visibleCellsData) {
            val cell = layoutCellData.cell
            if (cell is GridCell) {
                val subGridHeight = if (cell.constraints.verticalAlign == VerticalAlign.FILL)
                    layoutData.getFullPaddedHeight(layoutCellData) else cell.content.layoutData.preferredHeight
                cell.content.calculateLayoutDataStep4(subGridHeight)
            }
        }
    }

    fun calculateOutsideGaps(width: Int, height: Int) {
        var left = 0
        var right = 0
        var top = 0
        var bottom = 0
        for (layoutCellData in layoutData.visibleCellsData) {
            val cell = layoutCellData.cell

            // Update visualPaddings
            val component = (cell as? ComponentCell)?.component
            val layout = component?.layout as? GridLayout
            if (layout != null && component.getClientProperty(GridLayoutComponentProperty.SUB_GRID_AUTO_VISUAL_PADDINGS) != false) {
                val preferredSizeData = layout.getPreferredSizeData(component)
                cell.constraints.visualPaddings = preferredSizeData.outsideGaps
            }

            val bounds = calculateBounds(layoutCellData, 0, 0)
            when (cell) {
                is ComponentCell -> {
                    left = min(left, bounds.x)
                    top = min(top, bounds.y)
                    right = max(right, bounds.x + bounds.width - width)
                    bottom = max(bottom, bounds.y + bounds.height - height)
                }
                is GridCell -> {
                    cell.content.calculateOutsideGaps(bounds.width, bounds.height)
                    val outsideGaps = cell.content.layoutData.outsideGaps
                    left = min(left, bounds.x - outsideGaps.left)
                    top = min(top, bounds.y - outsideGaps.top)
                    right = max(right, bounds.x + bounds.width + outsideGaps.right - width)
                    bottom = max(bottom, bounds.y + bounds.height + outsideGaps.bottom - height)
                }
            }
        }
        layoutData.outsideGaps = Gaps(top = -top, left = -left, bottom = bottom, right = right)
    }

    fun getConstraints(component: JComponent): Constraints? {
        for (cell in cells) {
            when(cell) {
                is ComponentCell -> {
                    if (cell.component == component) {
                        return cell.constraints
                    }
                }

                is GridCell -> {
                    val constraints = cell.content.getConstraints(component)
                    if (constraints != null) {
                        return constraints
                    }
                }
            }
        }
        return null
    }

    /**
     * Calculate bounds for [layoutCellData]
     */
    private fun calculateBounds(layoutCellData: LayoutCellData, offsetX: Int, offsetY: Int): Rectangle {
        val cell = layoutCellData.cell
        val constraints = cell.constraints
        val visualPaddings = constraints.visualPaddings
        val paddedWidth = layoutData.getPaddedWidth(layoutCellData)
        val fullPaddedWidth = layoutData.getFullPaddedWidth(layoutCellData)
        val x = layoutData.columnsCoord[constraints.x] + constraints.gaps.left + layoutCellData.columnGaps.left - visualPaddings.left +
                when (constraints.horizontalAlign) {
                    HorizontalAlign.LEFT -> 0
                    HorizontalAlign.CENTER -> (fullPaddedWidth - paddedWidth) / 2
                    HorizontalAlign.RIGHT -> fullPaddedWidth - paddedWidth
                    HorizontalAlign.FILL -> 0
                }

        val fullPaddedHeight = layoutData.getFullPaddedHeight(layoutCellData)
        val paddedHeight = if (constraints.verticalAlign == VerticalAlign.FILL)
            fullPaddedHeight
        else
            min(fullPaddedHeight, layoutCellData.preferredSize.height - visualPaddings.height)
        val y: Int
        val baseline = layoutCellData.baseline
        if (baseline == null) {
            y = layoutData.rowsCoord[constraints.y] + layoutCellData.rowGaps.top + constraints.gaps.top - visualPaddings.top +
                    when (constraints.verticalAlign) {
                        VerticalAlign.TOP -> 0
                        VerticalAlign.CENTER -> (fullPaddedHeight - paddedHeight) / 2
                        VerticalAlign.BOTTOM -> fullPaddedHeight - paddedHeight
                        VerticalAlign.FILL -> 0
                    }
        }
        else {
            val rowBaselineData = layoutData.baselineData.get(layoutCellData)!!
            val rowHeight = layoutData.getHeight(layoutCellData)
            y = layoutData.rowsCoord[constraints.y] + calculateBaseline(rowHeight, constraints.verticalAlign, rowBaselineData) - baseline
        }

        return Rectangle(offsetX + x, offsetY + y, paddedWidth + visualPaddings.width, paddedHeight + visualPaddings.height)
    }

    /**
     * Calculates baseline for specified [height]
     */
    private fun calculateBaseline(height: Int, verticalAlign: VerticalAlign, rowBaselineData: RowBaselineData): Int {
        return rowBaselineData.maxAboveBaseline +
                when (verticalAlign) {
                    VerticalAlign.TOP -> 0
                    VerticalAlign.CENTER -> (height - rowBaselineData.height) / 2
                    VerticalAlign.BOTTOM -> height - rowBaselineData.height
                    VerticalAlign.FILL -> 0
                }
    }

    private fun isEmpty(constraints: Constraints): Boolean {
        for (cell in cells) {
            with(cell.constraints) {
                if (constraints.x + constraints.width > x &&
                    x + width > constraints.x &&
                    constraints.y + constraints.height > y &&
                    y + height > constraints.y
                ) {
                    return false
                }
            }
        }
        return true
    }
}