package org.ergoplatform.mosaik

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.actions.*

open class MosaikRuntime(
    val coroutineScope: () -> CoroutineScope,
    val mosaikContext: MosaikContext,
    val backendConnector: MosaikBackendConnector,
    /**
     * Handler to show and manage modal dialogs. These dialogs are managed outside Mosaik's
     * [ViewTree] so that implementations can use platform's modal dialogs
     */
    val showDialog: (MosaikDialog) -> Unit,
    val pasteToClipboard: (text: String) -> Unit,
    val openBrowser: (url: String) -> Boolean,
) {
    private val _uiLocked = MutableStateFlow(false)
    val uiLockedState: StateFlow<Boolean> get() = _uiLocked

    val viewTree = ViewTree(this)
    var appManifest: MosaikManifest? = null
        private set

    open fun runAction(action: Action, viewTree: ViewTree) {
        try {
            when (action) {
                is ChangeSiteAction -> {
                    runChangeSiteAction(action, viewTree)
                }
                is DialogAction -> {
                    runDialogAction(action, viewTree)
                }
                is CopyClipboardAction -> {
                    runCopyClipboardAction(action, viewTree)
                }
                is BrowserAction -> {
                    runBrowserAction(action, viewTree)
                }
                else -> TODO("Action type ${action.javaClass.simpleName} not yet implemented")
            }
        } catch (t: Throwable) {
            MosaikLogger.logError("Error running ${action.javaClass.simpleName}", t)
        }
    }

    private fun runCopyClipboardAction(action: CopyClipboardAction, viewTree: ViewTree) {
        pasteToClipboard(action.text)
    }

    open fun runBrowserAction(action: BrowserAction, viewTree: ViewTree) {
        val success = openBrowser(action.url)
        if (!success) {
            showDialog(
                MosaikDialog(
                    action.url, "OK",
                    null,
                    null,
                    null
                )
            )
        }
    }

    open fun runDialogAction(action: DialogAction, viewTree: ViewTree) {
        showDialog(
            MosaikDialog(
                action.message,
                action.positiveButtonText ?: "OK",
                action.negativeButtonText,
                viewTree.getAction(action.onPositiveButtonClicked)
                    ?.let { { runAction(it, viewTree) } },
                viewTree.getAction(action.onNegativeButtonClicked)
                    ?.let { { runAction(it, viewTree) } }
            )
        )
    }

    open fun runChangeSiteAction(action: ChangeSiteAction, viewTree: ViewTree) {
        try {
            viewTree.setContentView(action.newContent.view.id, action.newContent)
        } catch (nf: ElementNotFoundException) {
            MosaikLogger.logInfo(
                "${action.javaClass.simpleName}: element ${nf.elementId} not found, replacing complete view tree",
                nf
            )
            viewTree.setContentView(null, action.newContent)
        }
    }

    open fun loadMosaikApp(url: String) {
        _uiLocked.value = true
        coroutineScope().launch(Dispatchers.IO) {
            try {
                val mosaikApp = backendConnector.loadMosaikApp(url, mosaikContext)
                appManifest = mosaikApp.first
                val viewContent = mosaikApp.second

                viewTree.setRootView(viewContent)
            } catch (t: Throwable) {
                MosaikLogger.logError("Error loading Mosaik app", t)
                // TODO report to user
            }

            _uiLocked.value = false
        }
    }
}

data class MosaikDialog(
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String?,
    val positiveButtonClicked: Runnable?,
    val negativeButtonClicked: Runnable?
)