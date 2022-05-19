package org.ergoplatform.mosaik

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.actions.*
import java.lang.IllegalStateException

open class MosaikRuntime(
    val coroutineScope: () -> CoroutineScope,
    val mosaikContext: MosaikContext,
    val backendConnector: MosaikBackendConnector,
    /**
     * Handler to show and manage modal dialogs. These dialogs are managed outside Mosaik's
     * [ViewTree] so that implementations can use platform's modal dialogs
     */
    val showDialog: (MosaikDialog) -> Unit,
    /**
     * Show error to user. Can be called on background thread
     */
    val showError: (t: Throwable) -> Unit = { error ->
        showDialog(
            MosaikDialog(
                "Error running Mosaik app: ${error.javaClass.simpleName} ${error.message}",
                "OK",
                null,
                null,
                null
            )
        )
    },
    val pasteToClipboard: (text: String) -> Unit,
    val openBrowser: (url: String) -> Boolean,
) {
    private val _uiLocked = MutableStateFlow(false)
    val uiLockedState: StateFlow<Boolean> get() = _uiLocked

    val viewTree = ViewTree(this)
    var appManifest: MosaikManifest? = null
        private set

    open fun runAction(action: Action) {
        try {
            when (action) {
                is ChangeSiteAction -> {
                    runChangeSiteAction(action)
                }
                is DialogAction -> {
                    runDialogAction(action)
                }
                is CopyClipboardAction -> {
                    runCopyClipboardAction(action)
                }
                is BrowserAction -> {
                    runBrowserAction(action)
                }
                is BackendRequestAction -> {
                    runBackendRequest(action)
                }
                else -> TODO("Action type ${action.javaClass.simpleName} not yet implemented")
            }
        } catch (t: Throwable) {
            MosaikLogger.logError("Error running ${action.javaClass.simpleName}", t)
        }
    }

    open fun runBackendRequest(action: BackendRequestAction) {
        _uiLocked.value = true
        coroutineScope().launch(Dispatchers.IO) {
            try {
                // TODO make sure all values are already updated and no delayed jobs are active
                val fetchActionResponse =
                    backendConnector.fetchAction(action.url, mosaikContext, viewTree.currentValues)
                val appVersion = fetchActionResponse.appVersion
                val newAction = fetchActionResponse.action

                if (appVersion != appManifest!!.appVersion) {
                    // TODO reload app
                    throw IllegalStateException("Appversion changed while running app.")
                } else
                    withContext(Dispatchers.Main) {
                        runAction(newAction)
                    }

            } catch (t: Throwable) {
                MosaikLogger.logError("Error loading Mosaik app", t)
                errorRaised(t)
            }

            _uiLocked.value = false
        }
    }

    private fun runCopyClipboardAction(action: CopyClipboardAction) {
        pasteToClipboard(action.text)
    }

    open fun runBrowserAction(action: BrowserAction) {
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

    open fun runDialogAction(action: DialogAction) {
        showDialog(
            MosaikDialog(
                action.message,
                action.positiveButtonText ?: "OK",
                action.negativeButtonText,
                viewTree.getAction(action.onPositiveButtonClicked)
                    ?.let { { runAction(it) } },
                viewTree.getAction(action.onNegativeButtonClicked)
                    ?.let { { runAction(it) } }
            )
        )
    }

    open fun runChangeSiteAction(action: ChangeSiteAction) {
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
                appManifest = mosaikApp.manifest

                viewTree.setRootView(mosaikApp)
            } catch (t: Throwable) {
                MosaikLogger.logError("Error loading Mosaik app", t)
                errorRaised(t)
            }

            _uiLocked.value = false
        }
    }

    private fun errorRaised(t: Throwable) {
        // TODO report error to error report url
        showError(t)
    }
}

data class MosaikDialog(
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String?,
    val positiveButtonClicked: Runnable?,
    val negativeButtonClicked: Runnable?
)