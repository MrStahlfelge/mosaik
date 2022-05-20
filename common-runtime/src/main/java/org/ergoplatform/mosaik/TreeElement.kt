package org.ergoplatform.mosaik

import kotlinx.coroutines.delay
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.input.InputElement
import org.ergoplatform.mosaik.model.ui.input.TextField
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

/**
 * holds a [ViewElement] and wraps it with convenience accessors
 */
class TreeElement(
    val element: ViewElement,
    val parent: TreeElement?,
    private val viewTree: ViewTree,
) {
    private val _children = ArrayList<TreeElement>()

    init {
        if (element is ViewGroup) {
            _children.addAll(element.children.map { TreeElement(it, this, viewTree) })
        }
    }

    val hasId get() = element.id != null

    val id get() = element.id

    val idOrHash get() = element.id ?: element.hashCode().toString()

    val hasValue get() = hasId && element is InputElement<*>

    val currentValue
        get() = if (hasValue) {
            viewTree.getCurrentValue(this)
        } else null

    /**
     * returns the initial value as set by the viewtree
     */
    val initialValue: Any? get() = if (hasValue) (element as InputElement<*>).value else null

    /**
     * see [ViewTree.contentVersion]
     */
    val contentVersion get() = viewTree.contentVersion

    val children: List<TreeElement> get() = _children

    val respondsToClick: Boolean get() = element.onClickAction != null

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

    fun clicked() {
        viewTree.onItemClicked(this)
    }

    fun longPressed() {
        viewTree.onItemLongClicked(this)
    }

    fun runActionFromUserInteraction(actionId: String?) {
        viewTree.runActionFromUserInteraction(actionId)
    }

    fun valueChanged(newValue: Any?) {
        if (element is TextField<*>) {
            // delay value change for 300 ms so that not every key stroke fires the event
            viewTree.registerJobFor(this) {
                delay(300)
                viewTree.onItemValueChanged(this, newValue)
            }
        } else {
            viewTree.onItemValueChanged(this, newValue)
        }

    }
}