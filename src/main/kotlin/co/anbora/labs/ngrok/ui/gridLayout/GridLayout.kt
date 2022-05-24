package co.anbora.labs.ngrok.ui.gridLayout

import co.anbora.labs.ngrok.ui.UiDslException
import co.anbora.labs.ngrok.ui.checkComponent
import co.anbora.labs.ngrok.ui.checkConstraints
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager2
import javax.swing.JComponent

class GridLayout : LayoutManager2 {

    /**
     * Root grid of layout
     */
    val rootGrid: Grid
        get() = _rootGrid

    private val _rootGrid = GridImpl()

    override fun addLayoutComponent(comp: Component?, constraints: Any?) {
        val checkedConstraints = checkConstraints(constraints)
        val checkedComponent = checkComponent(comp)

        (checkedConstraints.grid as GridImpl).register(checkedComponent, checkedConstraints)
    }

    /**
     * Creates sub grid in the specified cell
     */
    fun addLayoutSubGrid(constraints: Constraints): Grid {
        if (constraints.widthGroup != null) {
            throw UiDslException("Sub-grids cannot use widthGroup: ${constraints.widthGroup}")
        }

        return (constraints.grid as GridImpl).registerSubGrid(constraints)
    }

    override fun addLayoutComponent(name: String?, comp: Component?) {
        throw UiDslException("Method addLayoutComponent(name: String?, comp: Component?) is not supported")
    }

    override fun removeLayoutComponent(comp: Component?) {
        if (!_rootGrid.unregister(checkComponent(comp))) {
            throw UiDslException("Component has not been registered: $comp")
        }
    }

    override fun preferredLayoutSize(parent: Container?): Dimension {
        if (parent == null) {
            throw UiDslException("Parent is null")
        }

        synchronized(parent.treeLock) {
            return getPreferredSizeData(parent).preferredSize
        }
    }

    override fun minimumLayoutSize(parent: Container?): Dimension {
        // May be we need to implement more accurate calculation later
        return preferredLayoutSize(parent)
    }

    override fun maximumLayoutSize(target: Container?) =
        Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)

    override fun layoutContainer(parent: Container?) {
        if (parent == null) {
            throw UiDslException("Parent is null")
        }

        synchronized(parent.treeLock) {
            _rootGrid.layout(parent.width, parent.height, parent.insets)
        }
    }

    override fun getLayoutAlignmentX(target: Container?) =
        // Just like other layout managers, no special meaning here
        0.5f

    override fun getLayoutAlignmentY(target: Container?) =
        // Just like other layout managers, no special meaning here
        0.5f

    override fun invalidateLayout(target: Container?) {
        // Nothing to do
    }

    fun getConstraints(component: JComponent): Constraints? {
        return _rootGrid.getConstraints(component)
    }

    internal fun getPreferredSizeData(parent: Container): PreferredSizeData {
        return _rootGrid.getPreferredSizeData(parent.insets)
    }
}