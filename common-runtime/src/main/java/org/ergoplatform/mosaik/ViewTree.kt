package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.input.InputElement
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * the complete tree of [ViewElement]'s.
 */
class ViewTree(val guid: String) {
    private var content: TreeElement? = null
    var cacheLifeTime: Long = 0

    private val idMap = HashMap<String, TreeElement>()
    private val valueMap = HashMap<String, Any?>()

    /**
     * replaces the view completely
     */
    fun setRootView(view: ViewElement, cacheLifeTime: Long) {
        setContentView(null, view)
        this.cacheLifeTime = cacheLifeTime
    }

    fun findElementById(id: String): TreeElement? {
        return idMap[id]
    }

    /**
     * replaces part of the view with the given id. If id is null, the complete view is replaced
     */
    fun setContentView(replaceId: String?, view: ViewElement) {
        val replacedElement = if (replaceId == null) content else findElementById(replaceId)

        if (replacedElement == null && replaceId != null) {
            throw IllegalArgumentException("Current view does not contain element with id $replaceId")
        }

        if (replacedElement == null && content == null) {
            content = TreeElement(view, null)
            // add all element ids and values to map
            addIdsAndValues(content!!)
        } else {
            val parent = replacedElement!!.parent
            val newTreeElement = TreeElement(view, parent)
            removeIdsAndValues(replacedElement)

            if (parent != null)
                parent.replaceChildElement(replacedElement, newTreeElement)
            else
                content = newTreeElement

            addIdsAndValues(newTreeElement)
        }
    }

    private fun addIdsAndValues(element: TreeElement) {
        element.visitAllElements { treeElement ->
            if (treeElement.hasId) {
                val newId = treeElement.id!!
                if (idMap.containsKey(newId)) {
                    throw IllegalArgumentException("There is already an element with id $newId part of current view.")
                }
                idMap[newId] = treeElement
                if (treeElement.hasValue)
                    valueMap[newId] = treeElement.value
            }
        }
    }

    private fun removeIdsAndValues(element: TreeElement) {
        element.visitAllElements { treeElement ->
            if (treeElement.hasId) {
                idMap.remove(treeElement.id!!)
                valueMap.remove(treeElement.id!!)
            }
        }
    }

    /**
     * Traverses all tree elements, including this one.
     * The visit order is sorted by the node depth in the tree, that means elements
     * less deep in the tree will be visited first
     */
    fun visitAllElements(visitor: (TreeElement) -> Unit) {
        content?.visitAllElements(visitor)
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

    val id get() = element.id

    val hasValue get() = hasId && element is InputElement<*>

    val value get() = if (hasValue) (element as InputElement<*>).value else null

    override fun equals(other: Any?): Boolean {
        return if (other is TreeElement) {
            element == other.element
        } else false
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }

    fun visitAllElements(visitor: (TreeElement) -> Unit) {
        val queue = LinkedList<TreeElement>()
        queue.add(this)

        while (queue.isNotEmpty()) {
            val first = queue.removeFirst()
            visitor(first)
            first.children.forEach { queue.add(it) }
        }
    }

    fun replaceChildElement(replacedElement: TreeElement, newTreeElement: TreeElement) {
        val indexOfChild = children.indexOf(replacedElement)
        if (indexOfChild < 0) {
            throw IllegalArgumentException("Could not find child to replace")
        }
        children[indexOfChild] = newTreeElement
        (element as ViewGroup).replaceChild(replacedElement.element, newTreeElement.element)
    }
}