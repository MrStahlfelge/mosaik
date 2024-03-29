package org.ergoplatform.mosaik

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.Image
import org.ergoplatform.mosaik.model.ui.LazyLoadBox
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.input.ErgoAddressChooseButton
import org.ergoplatform.mosaik.model.ui.input.InputElement
import org.ergoplatform.mosaik.model.ui.input.TextField
import org.ergoplatform.mosaik.model.ui.input.WalletChooseButton
import org.ergoplatform.mosaik.model.ui.layout.Box
import org.ergoplatform.mosaik.model.ui.text.ErgoAddressLabel
import org.ergoplatform.mosaik.model.ui.text.TokenLabel
import kotlin.math.max

/**
 * the complete tree of [ViewElement]'s and is context.
 */
class ViewTree(val mosaikRuntime: MosaikRuntime) {
    var content: TreeElement? = null
        private set
    val targetCanvasDimension get() = mosaikRuntime.appManifest?.targetCanvasDimension

    private val idMap = HashMap<String, TreeElement>()
    private val valueMap = HashMap<String, CheckedValue>()
    private val jobMap = HashMap<String, Job>()
    private val actionMap = HashMap<String, Action>()
    private val resourceMap = HashMap<String, ByteArray>()

    /**
     * set to true while view tree is altered, to prevent notifying consumers in this state
     */
    private var changingViewTree = false

    var lastViewTreeChangeMs: Long = 0
        private set

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
    val valueState: StateFlow<Pair<Int, Map<String, CheckedValue>>> get() = _valueFlow
    private val _valueFlow =
        MutableStateFlow<Pair<Int, Map<String, CheckedValue>>>(Pair(0, valueMap))

    /**
     * ui is locked when backend requests are running
     */
    val uiLockedState: StateFlow<Boolean> get() = _uiLocked
    private val _uiLocked = MutableStateFlow(false)
    val uiLocked: Boolean get() = _uiLocked.value
    private var lockCount = 0 // we have recursive locking of the UI, boolean flag is not sufficient

    fun lockUi() {
        lockCount++
        _uiLocked.value = true
    }

    fun unlockUi() {
        lockCount = max(lockCount - 1, 0)
        _uiLocked.value = lockCount > 0
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
            lastViewTreeChangeMs = System.currentTimeMillis()
            notifyViewTreeChanged()
        }
    }

    private fun notifyViewTreeChanged() {
        if (!changingViewTree) {
            _modificationFlow.value = Pair(_modificationFlow.value.first + 1, content)
            mosaikRuntime.viewTreeChanged()
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
                    valueMap[newId] =
                        getCurrentValue(treeElement)
                            ?: treeElement.initialValue.let { initialValue ->
                                CheckedValue(initialValue, treeElement.isValueValid(initialValue))
                            }
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
                    replaceViewElement(
                        treeElement,
                        ViewContent(lazyLoadBox.errorView ?: Box())
                    )
                }
            }

        }
    }

    private fun startImageDownload(treeElement: TreeElement) {
        registerJobFor(treeElement) { scope ->
            MosaikLogger.logDebug("Start downloading image for ${treeElement.idOrUuid}...")
            withContext(Dispatchers.IO) {
                val bytes = mosaikRuntime.downloadImage((treeElement.element as Image).url, scope)
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
            val newJob = mosaikRuntime.coroutineScope.launch {
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
    fun onItemClicked(treeElement: TreeElement) {
        val element = treeElement.element
        when (element) {
            is ErgoAddressChooseButton -> {
                treeElement.id?.let { mosaikRuntime.showErgoAddressChooser(it) }
            }
            is WalletChooseButton -> {
                treeElement.id?.let { mosaikRuntime.showErgoWalletChooser(it) }
            }
            is TokenLabel -> {
                if (element.onClickAction != null)
                    runActionFromUserInteraction(element.onClickAction)
                else
                    mosaikRuntime.runTokenInformationAction(element.tokenId)
            }
            else -> runActionFromUserInteraction(element.onClickAction)
        }
    }

    /**
     * called when user long pressed an element
     */
    fun onItemLongClicked(treeElement: TreeElement) {
        val element = treeElement.element
        when (element) {
            is ErgoAddressLabel -> {
                if (element.onLongPressAction == null)
                    element.text?.let { mosaikRuntime.onAddressLongPress(it) }
                else
                    runActionFromUserInteraction(element.onLongPressAction)
            }
            else -> runActionFromUserInteraction(element.onLongPressAction)
        }
    }

    fun runActionFromUserInteraction(actionId: String?) {
        if (!uiLocked) {
            getAction(actionId)?.let { mosaikRuntime.runAction(it) }
        }
    }

    fun onItemValueChanged(treeElement: TreeElement, newValue: Any?, isValid: Boolean) {
        if (treeElement.hasId) {
            val id = treeElement.id!!
            if (valueMap[id]?.inputValue != newValue) {
                valueMap[id] = CheckedValue(newValue, isValid)
                notifyValuesChanged()
                MosaikLogger.logInfo("Value $id changed to $newValue")
                getAction((treeElement.element as? InputElement<*>)?.onValueChangedAction)?.let { action ->
                    if (treeElement.element is TextField<*> && mosaikRuntime.textFieldOnValueChangedActionDelay > 0) {
                        // delay value change so that not every key stroke fires the event
                        registerJobFor(treeElement) { coroutine ->
                            delay(mosaikRuntime.textFieldOnValueChangedActionDelay)
                            if (coroutine.isActive) {
                                mosaikRuntime.runAction(action)
                            }
                        }
                    } else {
                        mosaikRuntime.runAction(action)
                    }
                }
            }
        }
    }

    /**
     * returns pair of current value and if the value is valid
     */
    fun getCurrentValue(treeElement: TreeElement): CheckedValue? =
        valueMap[treeElement.id]

    fun getResourceBytes(treeElement: TreeElement): ByteArray? =
        resourceMap[treeElement.idOrUuid]

    /**
     * throws [InvalidValuesException] if invalid values are entered by user
     */
    fun ensureValuesAreCorrect() {
        val invalidPairs =
            valueMap.entries.filter { valueValidEntry -> !valueValidEntry.value.valid }

        if (invalidPairs.isNotEmpty()) {
            val errorList =
                invalidPairs.map { entry -> findElementById(entry.key)!!.getInvalidValueError() }
                    .joinToString("\n")

            throw InvalidValuesException("Please enter valid inputs for\n$errorList", errorList)
        }
    }

    /**
     * returns map (id, Pair(value, validity))
     */
    val currentValues: Map<String, CheckedValue> get() = valueMap

    /**
     * returns map (id, value) for valid values
     */
    val currentValidValues: Map<String, Any?> get() = valueMap.mapValues { if (it.value.valid) it.value.inputValue else null }

    data class CheckedValue(val inputValue: Any?, val valid: Boolean)
}