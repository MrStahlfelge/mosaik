package org.ergoplatform.mosaik

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.input.InputElement

/**
 * the complete tree of [ViewElement]'s and is context.
 */
class ViewTree(val mosaikRuntime: MosaikRuntime) {
    var content: TreeElement? = null
        private set

    private val idMap = HashMap<String, TreeElement>()
    private val valueMap = HashMap<String, Any?>()
    private val jobMap = HashMap<String, Job>()
    private val actionMap = HashMap<String, Action>()

    /**
     * flow that emits when viewtree is changed
     */
    val contentState: StateFlow<Pair<Int, TreeElement?>> get() = _modificationFlow
    private val _modificationFlow = MutableStateFlow<Pair<Int, TreeElement?>>(Pair(0, null))

    val actions get() = actionMap.values.toList()

    /**
     * contentVersion is incremented every time the ViewTree is changed
     */
    val contentVersion get() = contentState.value.first

    /**
     * flow that emits when values map is changed
     */
    val valueState: StateFlow<Pair<Int, Map<String, Any?>>> get() = _valueFlow
    private val _valueFlow = MutableStateFlow<Pair<Int, Map<String, Any?>>>(Pair(0, valueMap))

    /**
     * replaces the view completely
     */
    fun setRootView(view: ViewContent) {
        actionMap.clear()
        setContentView(null, view)
    }

    fun findElementById(id: String): TreeElement? {
        return idMap[id]
    }

    /**
     * replaces part of the view with the given id. If id is null, the complete view is replaced
     */
    fun setContentView(replaceId: String?, newContent: ViewContent) {
        val view = newContent.view
        val replacedElement = if (replaceId == null) content else findElementById(replaceId)

        if (replacedElement == null && replaceId != null) {
            throw ElementNotFoundException(
                "Current view does not contain element with id $replaceId",
                replaceId
            )
        }

        newContent.actions.forEach { action -> actionMap[action.id] = action }

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
        notifyViewTreeChanged()
    }

    private fun notifyViewTreeChanged() {
        _modificationFlow.value = Pair(_modificationFlow.value.first + 1, content)
    }

    private fun addIdsAndValues(element: TreeElement) {
        var valuesChanged = false
        element.visitAllElements { treeElement ->
            if (treeElement.hasId) {
                val newId = treeElement.id!!
                if (idMap.containsKey(newId)) {
                    throw IllegalArgumentException("There is already an element with id $newId part of current view.")
                }
                idMap[newId] = treeElement
                if (treeElement.hasValue) {
                    valueMap[newId] = getCurrentValue(treeElement) ?: treeElement.initialValue
                    valuesChanged = true
                }
            }
        }
        if (valuesChanged) {
            notifyValuesChanged()
        }
    }

    private fun removeIdsAndValues(element: TreeElement) {
        val size = valueMap.size
        element.visitAllElements { treeElement ->
            cancelRunningJobFor(element)
            if (treeElement.hasId) {
                idMap.remove(treeElement.id!!)
                valueMap.remove(treeElement.id!!)
            }
        }
        if (valueMap.size != size) {
            notifyValuesChanged()
        }
    }

    fun cancelRunningJobFor(element: TreeElement) {
        synchronized(jobMap) {
            val idOrHash = element.idOrHash
            val job = jobMap[idOrHash]
            job?.let {
                if (!job.isCompleted) job.cancel()
                jobMap.remove(idOrHash)
            }
        }
    }

    /**
     * Registers a new Job for the element. A former running job will be cancelled
     */
    fun registerJobFor(element: TreeElement, job: suspend () -> Unit) {
        synchronized(jobMap) {
            cancelRunningJobFor(element)
            val newJob = mosaikRuntime.coroutineScope().launch {
                job()
            }
            jobMap[element.idOrHash] = newJob
        }
    }

    private fun notifyValuesChanged() {
        _valueFlow.value = Pair(_valueFlow.value.first + 1, currentValues)
    }

    /**
     * Traverses all tree elements, including this one.
     * The visit order is sorted by the node depth in the tree, that means elements
     * less deep in the tree will be visited first
     */
    fun visitAllElements(visitor: (TreeElement) -> Unit) {
        content?.visitAllElements(visitor)
    }

    fun getAction(id: String?): Action? =
        id?.let {
            val action = actionMap[id]
            if (action == null)
                MosaikLogger.logError("Action with id $id referenced, but not available")
            action
        }

    /**
     * called when user clicked or tapped an element
     */
    fun onItemClicked(element: TreeElement) {
        getAction(element.element.onClickAction)?.let {
            mosaikRuntime.runAction(it, this)
        }
    }

    /**
     * called when user long pressed an element
     */
    fun onItemLongClicked(element: TreeElement) {
        getAction(element.element.onLongPressAction)?.let {
            mosaikRuntime.runAction(it, this)
        }
    }

    fun onItemValueChanged(treeElement: TreeElement, newValue: Any?) {
        if (treeElement.hasId) {
            val id = treeElement.id!!
            if (valueMap[id] != newValue) {
                valueMap[id] = newValue
                notifyValuesChanged()
                MosaikLogger.logInfo("Value $id changed to $newValue")
                getAction((treeElement.element as? InputElement<*>)?.onValueChangedAction)?.let { action ->
                    mosaikRuntime.runAction(action, this)
                }
            }
        }
    }

    fun getCurrentValue(treeElement: TreeElement): Any? =
        valueMap[treeElement.id]

    val currentValues: Map<String, Any?> get() = valueMap
}