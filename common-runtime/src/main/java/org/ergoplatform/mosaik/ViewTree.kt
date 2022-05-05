package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.input.InputElement

/**
 * the complete tree of [ViewElement]'s.
 */
class ViewTree(val guid: String) {
    private var content: TreeElement? = null
    var cacheLifeTime: Long = 0

    private val idMap = HashMap<String, TreeElement>()
    private val valueMap = HashMap<String, Any>()

    /**
     * replaces the view completely
     */
    fun setRootView(view: ViewElement, cacheLifeTime: Long) {
        setContentView(null, view)
        this.cacheLifeTime = cacheLifeTime
    }

    /**
     * replaces part of the view with the given id.
     */
    fun setContentView(replaceId: String?, view: ViewElement) {

    }
}

/**
 * holds a [ViewElement] and wraps it with convenience accessors
 */
class TreeElement(
    val element: ViewElement,
    val parent: TreeElement?
) {
    private val children = ArrayList<TreeElement>()

    init {
        if (element is ViewGroup) {
            children.addAll(element.children.map { TreeElement(it, this) })
        }
    }

    val hasId get() = element.id != null

    val hasValue get() = hasId && element is InputElement<*>

    override fun equals(other: Any?): Boolean {
        return if (other is TreeElement) {
            element == other.element
        } else false
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}