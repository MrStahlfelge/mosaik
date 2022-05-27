package org.ergoplatform.mosaik

import kotlinx.coroutines.*
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
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
    /**
     * Show error to user. Can be called on background thread
     */
    val showError: (t: Throwable) -> Unit = { error ->
        showDialog(
            MosaikDialog(
                "Error:\n${error.message}\n(${error.javaClass.simpleName})",
                "OK",
                null,
                null,
                null
            )
        )
    },
    val pasteToClipboard: (text: String) -> Unit,
    val openBrowser: (url: String) -> Boolean,
    val appLoaded: ((MosaikManifest) -> Unit)? = null,
) {

    val viewTree = ViewTree(this)
    var appManifest: MosaikManifest? = null
        private set
    private var actualAppUrl: String? = null
    val appBaseUrl get() = appManifest?.baseUrl ?: actualAppUrl

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
                    loadMosaikApp(appBaseUrl!!)
                }
                else -> TODO("Action type ${action.javaClass.simpleName} not yet implemented")
            }
        } catch (e: InvalidValuesException) {
            // show user, do not log an error
            showError(e)
        } catch (e: ChangeViewContentException) {
            errorRaised(e)
        } catch (t: Throwable) {
            MosaikLogger.logError("Error running ${action.javaClass.simpleName}", t)
        }
    }

    open fun runNavigateAction(action: NavigateAction) {
        loadMosaikApp(action.url, actualAppUrl)
    }

    open fun runBackendRequest(action: BackendRequestAction) {
        viewTree.ensureValuesAreCorrect()

        viewTree.uiLocked = true
        coroutineScope().launch(Dispatchers.IO) {
            try {
                val fetchActionResponse =
                    backendConnector.fetchAction(
                        action.url,
                        appBaseUrl,
                        mosaikContext,
                        viewTree.currentValidValues,
                        actualAppUrl
                    )
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
                MosaikLogger.logError("Error running Mosaik backend request", t)
                errorRaised(t)
            }

            viewTree.uiLocked = false
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
        coroutineScope().launch(Dispatchers.IO) {
            try {
                val mosaikApp = backendConnector.loadMosaikApp(url, mosaikContext, referrer)
                appManifest = mosaikApp.manifest

                viewTree.setRootView(mosaikApp)
                actualAppUrl = url
                appLoaded?.invoke(mosaikApp.manifest)
            } catch (t: Throwable) {
                // TODO errors during first app loading (with empty screen) should be handled
                //  different from errors
                // while the app is running to not present an empty view
                // instead, show a built-in error screen with a Retry button
                MosaikLogger.logError("Error loading Mosaik app", t)
                errorRaised(t)
            }

            viewTree.uiLocked = false
        }
    }

    /**
     * an error that is shown to user and reported to the error report url
     */
    private fun errorRaised(t: Throwable) {
        // TODO report error to error report url
        showError(t)
    }

    /**
     * Downloads an image, blocking. In case of an error an empty byte array is returned
     */
    fun downloadImage(url: String): ByteArray {
        return try {
            backendConnector.fetchImage(url, appBaseUrl, actualAppUrl)
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
            backendConnector.fetchLazyContent(url, appBaseUrl, mosaikContext, actualAppUrl!!)
        } catch (t: Throwable) {
            MosaikLogger.logError("Could not fetch content $url", t)
            null
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