package org.ergoplatform.mosaik

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.*
import org.ergoplatform.mosaik.model.ui.input.InputElement
import org.ergoplatform.mosaik.model.ui.layout.Box

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
    private val resourceMap = HashMap<String, ByteArray>()

    /**
     * set to true while view tree is altered, to prevent notifying consumers in this state
     */
    private var changingViewTree = false

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
     * ui is locked when backend requests are running
     */
    val uiLockedState: StateFlow<Boolean> get() = _uiLocked
    private val _uiLocked = MutableStateFlow(false)
    var uiLocked: Boolean
        get() = _uiLocked.value
        set(value) {
            _uiLocked.value = value
        }

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
        val replacedElement = if (replaceId == null) content else findElementById(replaceId)

        if (replacedElement == null && replaceId != null) {
            throw ElementNotFoundException(
                "Current view does not contain element with id $replaceId",
                replaceId
            )
        }

        replaceViewElement(replacedElement, newContent)
    }

    private fun replaceViewElement(replacedElement: TreeElement?, newContent: ViewContent) {
        synchronized(this) {
            val view = newContent.view

            newContent.actions.forEach { action -> actionMap[action.id] = action }

            changingViewTree = true
            if (replacedElement == null && content == null) {
                content = TreeElement(view, null, this)
                // add all element ids and values to map
                addIdsJobsAndValues(content!!)
            } else {
                val parent = replacedElement!!.parent
                val newTreeElement = TreeElement(view, parent, this)
                removeIdsJobsAndValues(replacedElement)

                if (parent != null)
                    parent.replaceChildElement(replacedElement, newTreeElement)
                else
                    content = newTreeElement

                addIdsJobsAndValues(newTreeElement)
            }
            changingViewTree = false
            notifyViewTreeChanged()
        }
    }

    private fun notifyViewTreeChanged() {
        if (!changingViewTree) {
            _modificationFlow.value = Pair(_modificationFlow.value.first + 1, content)
        }
    }

    private fun addIdsJobsAndValues(element: TreeElement) {
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
            } else if (treeElement.element is InputElement<*>) {
                MosaikLogger.logWarning("Input element without id found: ${treeElement.element.javaClass.simpleName}")
            }
            when (treeElement.element) {
                is Image -> startImageDownload(treeElement)
                is LazyLoadBox -> startFetchLazyContent(treeElement)
            }
        }
        if (valuesChanged) {
            notifyValuesChanged()
        }
    }

    private fun startFetchLazyContent(treeElement: TreeElement) {
        registerJobFor(treeElement) { scope ->
            MosaikLogger.logDebug("Start fetching contents for ${treeElement.idOrUuid}...")
            val lazyLoadBox = treeElement.element as LazyLoadBox
            val newContent = withContext(Dispatchers.IO) {
                mosaikRuntime.fetchLazyContents(lazyLoadBox.requestUrl)
            }
            if (scope.isActive) {
                if (newContent != null) {
                    replaceViewElement(treeElement, newContent)
                } else {
                    replaceViewElement(treeElement, lazyLoadBox.errorView ?: ViewContent(Box()))
                }
            }

        }
    }

    private fun startImageDownload(treeElement: TreeElement) {
        registerJobFor(treeElement) { scope ->
            MosaikLogger.logDebug("Start downloading image for ${treeElement.idOrUuid}...")
            withContext(Dispatchers.IO) {
                val bytes = mosaikRuntime.downloadImage((treeElement.element as Image).url)
                if (scope.isActive) {
                    MosaikLogger.logDebug("Downloading image for ${treeElement.idOrUuid} done.")
                    resourceMap[treeElement.idOrUuid] = bytes
                    notifyViewTreeChanged()
                }
            }
        }
    }

    private fun removeIdsJobsAndValues(element: TreeElement) {
        val size = valueMap.size
        element.visitAllElements { treeElement ->
            cancelRunningJobFor(treeElement)
            if (treeElement.hasId) {
                idMap.remove(treeElement.id!!)
                valueMap.remove(treeElement.id!!)
            }
            resourceMap.remove(treeElement.idOrUuid)
        }
        if (valueMap.size != size) {
            notifyValuesChanged()
        }
    }

    fun cancelRunningJobFor(element: TreeElement) {
        synchronized(jobMap) {
            val idOrHash = element.idOrUuid
            val job = jobMap[idOrHash]
            job?.let {
                if (!job.isCompleted) {
                    MosaikLogger.logDebug("Cancelling job for ${element.idOrUuid}")
                    job.cancel()
                }
                jobMap.remove(idOrHash)
            }
        }
    }

    /**
     * Registers a new Job for the element. A former running job will be cancelled
     */
    fun registerJobFor(element: TreeElement, job: suspend (CoroutineScope) -> Unit) {
        synchronized(jobMap) {
            cancelRunningJobFor(element)
            val newJob = mosaikRuntime.coroutineScope().launch {
                job(this)
            }
            jobMap[element.idOrUuid] = newJob
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
        runActionFromUserInteraction(element.element.onClickAction)
    }

    /**
     * called when user long pressed an element
     */
    fun onItemLongClicked(element: TreeElement) {
        runActionFromUserInteraction(element.element.onLongPressAction)
    }

    fun runActionFromUserInteraction(actionId: String?) {
        if (!uiLocked) {
            getAction(actionId)?.let { mosaikRuntime.runAction(it) }
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
                    mosaikRuntime.runAction(action)
                }
            }
        }
    }

    fun getCurrentValue(treeElement: TreeElement): Any? =
        valueMap[treeElement.id]

    fun getResourceBytes(treeElement: TreeElement): ByteArray? =
        resourceMap[treeElement.idOrUuid]

    fun ensureValuesAreUpdated() {
        // TODO make sure all values are already updated and no delayed jobs are active
    }

    val currentValues: Map<String, Any?> get() = valueMap
}