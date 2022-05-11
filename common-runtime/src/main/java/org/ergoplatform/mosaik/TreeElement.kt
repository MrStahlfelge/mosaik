package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.input.InputElement
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

/**
 * holds a [ViewElement] and wraps it with convenience accessors
 */
class TreeElement(
    val element: ViewElement,
    val parent: TreeElement?,
    val viewTree: ViewTree,
) {
    private val _children = ArrayList<TreeElement>()

    init {
        if (element is ViewGroup) {
            _children.addAll(element.children.map { TreeElement(it, this, viewTree) })
        }
    }

    val hasId get() = element.id != null

    val id get() = element.id

    val hasValue get() = hasId && element is InputElement<*>

    val value get() = if (hasValue) (element as InputElement<*>).value else null

    val children: List<TreeElement> get() = _children

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
            first._children.forEach { queue.add(it) }
        }
    }

    fun replaceChildElement(replacedElement: TreeElement, newTreeElement: TreeElement) {
        val indexOfChild = _children.indexOf(replacedElement)
        if (indexOfChild < 0) {
            throw IllegalArgumentException("Could not find child to replace")
        }
        _children[indexOfChild] = newTreeElement
        (element as ViewGroup).replaceChild(replacedElement.element, newTreeElement.element)
    }
}