package org.ergoplatform.mosaik

import kotlinx.coroutines.*
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.*
import java.util.*

abstract class MosaikRuntime(
    val backendConnector: MosaikBackendConnector,
) {

    abstract val coroutineScope: CoroutineScope

    /**
     * Handler to show and manage modal dialogs. These dialogs are managed outside Mosaik's
     * [ViewTree] so that implementations can use platform's modal dialogs
     */
    abstract fun showDialog(dialog: MosaikDialog)

    abstract fun pasteToClipboard(text: String)

    abstract fun openBrowser(url: String)

    var appLoaded: ((MosaikManifest) -> Unit)? = null

    /**
     * Show error to user. Can be called on background thread
     */
    open fun showError(error: Throwable) {
        showDialog(
            MosaikDialog(
                "Error:\n${error.message}\n(${error.javaClass.simpleName})",
                "OK",
                null,
                null,
                null
            )
        )
    }

    val viewTree = ViewTree(this)
    var appManifest: MosaikManifest? = null
        private set
    private val _appUrlStack = LinkedList<UrlHistoryEntry>()
    val appUrlHistory: List<String> get() = _appUrlStack.map { it.url }
    val appUrl: String? get() = if (_appUrlStack.isNotEmpty()) appUrlHistory.first() else null

    fun runAction(actionId: String) {
        viewTree.getAction(actionId)?.let { runAction(it) }
    }

    open fun runAction(action: Action) {
        MosaikLogger.logDebug("Running action ${action.id}...")
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
                is NavigateAction -> {
                    runNavigateAction(action)
                }
                is ReloadAction -> {
                    loadMosaikApp(appUrl!!)
                }
                is ErgoPayAction -> {
                    runErgoPayAction(action)
                }
                is TokenInformationAction -> {
                    runTokenInformationAction(action)
                }
                // TODO ErgoAuthAction
                // TODO QrCodeAction
                else -> runUnknownAction(action)
            }
        } catch (e: InvalidValuesException) {
            // show user, do not log an error
            showError(e)
        } catch (e: ChangeViewContentException) {
            raiseError(e)
        } catch (t: Throwable) {
            MosaikLogger.logError("Error running ${action.javaClass.simpleName}", t)
        }
    }

    abstract fun runTokenInformationAction(action: TokenInformationAction)

    abstract fun runErgoPayAction(action: ErgoPayAction)

    open fun runUnknownAction(action: Action) {
        throw UnsupportedOperationException("Action type ${action.javaClass.simpleName} not yet implemented")
    }

    open fun runNavigateAction(action: NavigateAction) {
        loadMosaikApp(action.url, appUrl)
    }

    open fun runBackendRequest(action: BackendRequestAction) {
        if (action.postValues == BackendRequestAction.PostValueType.ALL)
            viewTree.ensureValuesAreCorrect()

        viewTree.uiLocked = true
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val fetchActionResponse =
                    backendConnector.fetchAction(
                        action.url,
                        appUrl,
                        if (action.postValues == BackendRequestAction.PostValueType.NONE) emptyMap()
                        else viewTree.currentValidValues,
                        appUrl
                    )
                val appVersion = fetchActionResponse.appVersion
                val newAction =
                    if (appVersion != appManifest!!.appVersion) ReloadAction()
                    else fetchActionResponse.action

                withContext(Dispatchers.Main) {
                    runAction(newAction)
                }

            } catch (t: Throwable) {
                MosaikLogger.logError("Error running Mosaik backend request", t)
                raiseError(t)
            }

            viewTree.uiLocked = false
        }

    }

    private fun runCopyClipboardAction(action: CopyClipboardAction) {
        pasteToClipboard(action.text)
    }

    open fun runBrowserAction(action: BrowserAction) {
        openBrowser(action.url)
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
            try {
                viewTree.setContentView(action.newContent.view.id, action.newContent)
            } catch (nf: ElementNotFoundException) {
                MosaikLogger.logInfo(
                    "${action.javaClass.simpleName}: element ${nf.elementId} not found, replacing complete view tree",
                    nf
                )
                viewTree.setContentView(null, action.newContent)
            }
        } catch (t: Throwable) {
            // we are probably in a very bad state now, tell the user to reload
            throw ChangeViewContentException(t)
        }
    }

    /**
     * starts loading a new Mosaik app. Please make sure this is not called twice simultaneously.
     */
    open fun loadMosaikApp(url: String, referrer: String? = null) {
        viewTree.uiLocked = true
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val loadAppResponse = backendConnector.loadMosaikApp(url, referrer)
                val mosaikApp = loadAppResponse.mosaikApp
                appManifest = mosaikApp.manifest

                viewTree.setRootView(mosaikApp)
                navigatedTo(UrlHistoryEntry(loadAppResponse.appUrl, referrer), mosaikApp.manifest)
            } catch (t: Throwable) {
                MosaikLogger.logError("Error loading Mosaik app", t)
                raiseError(t)
            }

            viewTree.uiLocked = false
        }
    }

    private fun navigatedTo(urlHistoryEntry: UrlHistoryEntry, manifest: MosaikManifest) {
        if (appUrl != urlHistoryEntry.url)
            _appUrlStack.addFirst(urlHistoryEntry)
        appLoaded?.invoke(manifest)
    }

    /**
     * Navigates back to the previous Mosaik App, if any and returns true.
     * Returns false if there us no previous Mosaik app
     */
    open fun navigateBack(): Boolean {
        return if (canNavigateBack()) {
            _appUrlStack.remove()
            val lastApp = _appUrlStack.first
            loadMosaikApp(lastApp.url, lastApp.referrer)
            true
        } else
            false
    }

    fun canNavigateBack() = _appUrlStack.size >= 2

    /**
     * an error that is shown to user and reported to the error report url
     */
    open fun raiseError(t: Throwable) {
        // TODO report error to error report url
        showError(t)
    }

    /**
     * Downloads an image, blocking. In case of an error an empty byte array is returned
     */
    fun downloadImage(url: String): ByteArray {
        return try {
            backendConnector.fetchImage(url, appUrl, appUrl)
        } catch (t: Throwable) {
            MosaikLogger.logWarning("Could not download image $url", t)
            ByteArray(0)
        }
    }

    /**
     * Downloads content of LazyLoadBox, blocking. In case of an error null is returned
     */
    fun fetchLazyContents(url: String): ViewContent? {
        return try {
            backendConnector.fetchLazyContent(url, appUrl, appUrl!!)
        } catch (t: Throwable) {
            MosaikLogger.logError("Could not fetch content $url", t)
            null
        }
    }

    abstract fun isErgoAddressValid(ergoAddress: String): Boolean

    /**
     * return an address label, if available
     */
    abstract fun getErgoAddressLabel(ergoAddress: String): String?

    abstract fun formatString(string: StringConstant, values: String? = null): String

    /**
     * open a chooser for an ergo address. If the user sets a new value, this should call
     * [setValue] with the new address.
     */
    abstract fun showErgoAddressChooser(valueId: String)

    /**
     * set the value for a value element in the current tree. Please note that not all elements
     * are automatically updated by this in the view.
     */
    fun setValue(valueId: String, newValue: Any?) {
        viewTree.findElementById(valueId)?.let { element ->
            if (element.hasValue)
                element.valueChanged(newValue)
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

data class UrlHistoryEntry(val url: String, val referrer: String?)

enum class StringConstant {
    ChooseAddress,
    PleaseChoose,
}