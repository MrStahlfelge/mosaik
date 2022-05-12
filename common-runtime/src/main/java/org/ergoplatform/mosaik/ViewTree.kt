package org.ergoplatform.mosaik

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * the complete tree of [ViewElement]'s and is context.
 */
class ViewTree(val guid: String, val actionRunner: ActionRunner) {
    var content: TreeElement? = null
        private set
    var cacheLifeTime: Long = 0
        private set
    private val _modificationFlow = MutableStateFlow<Pair<Int, TreeElement?>>(Pair(0, null))
    val contentState: StateFlow<Pair<Int, TreeElement?>> get() = _modificationFlow

    /**
     * contentVersion is incremented every time the ViewTree is changed
     */
    val contentVersion get() = contentState.value.first

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
            throw ElementNotFoundException(
                "Current view does not contain element with id $replaceId",
                replaceId
            )
        }

        if (replacedElement == null && content == null) {
            content = TreeElement(view, null, this)
            // add all element ids and values to map
            addIdsAndValues(content!!)
        } else {
            val parent = replacedElement!!.parent
            val newTreeElement = TreeElement(view, parent, this)
            removeIdsAndValues(replacedElement)

            if (parent != null)
                parent.replaceChildElement(replacedElement, newTreeElement)
            else
                content = newTreeElement

            addIdsAndValues(newTreeElement)
        }
        _modificationFlow.value = Pair(_modificationFlow.value.first + 1, content)
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
                    valueMap[newId] = getCurrentValue(treeElement) ?: treeElement.initialValue
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

    /**
     * called when user clicked or tapped an element
     */
    fun onItemClicked(element: TreeElement) {
        element.element.onClickAction?.let {
            actionRunner.runAction(it, this)
        }
    }

    fun onItemValueChanged(treeElement: TreeElement, newValue: Any?) {
        if (treeElement.hasId) {
            valueMap[treeElement.id!!] = newValue
        }
    }

    fun getCurrentValue(treeElement: TreeElement): Any? =
        valueMap[treeElement.id]
}