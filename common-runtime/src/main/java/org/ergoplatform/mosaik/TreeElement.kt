package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.input.InputElement
import org.ergoplatform.mosaik.model.ui.input.IntegerTextField
import org.ergoplatform.mosaik.model.ui.input.TextInputField
import java.util.*

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

    val idOrUuid by lazy { element.id ?: UUID.randomUUID().toString() }

    val hasValue get() = hasId && element is InputElement<*>

    val currentValue
        get() = if (hasValue) {
            viewTree.getCurrentValue(this)?.inputValue
        } else null

    val getResourceBytes get() = viewTree.getResourceBytes(this)

    /**
     * returns the initial value as set by the viewtree
     */
    val initialValue: Any? get() = if (hasValue) (element as InputElement<*>).value else null

    /**
     * see [ViewTree.contentVersion]
     * This is not a get() by intention. Content version of the tree when this element was added.
     */
    val createdAtContentVersion = viewTree.contentVersion

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

    fun valueChanged(newValue: Any?): Boolean {
        val isValid = isValueValid(newValue)
        viewTree.onItemValueChanged(this, newValue, isValid)
        return isValid
    }

    fun isValueValid(value: Any?): Boolean {
        return when (element) {
            is TextInputField -> {
                val length = (value as? String)?.length ?: 0
                length >= element.minValue && length <= element.maxValue
            }
            is IntegerTextField -> {
                // TODO check
                value is Long && value >= element.minValue && value <= element.maxValue
            }
            else -> {
                // TODO mandatory elements should return false
                true
            }
        }
    }

    fun getInvalidValueError(): String {
        return when (element) {
            is TextInputField -> {
                element.errorMessage ?: element.placeholder ?: element.id!!
            } else -> {
                id!!
            }
        }
    }
}