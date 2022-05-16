package org.ergoplatform.mosaik

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import org.ergoplatform.mosaik.model.actions.*

open class ActionRunner(
    val coroutineScope: () -> CoroutineScope,
    /**
     * Handler to show and manage modal dialogs. These dialogs are managed outside Mosaik's
     * [ViewTree] so that implementations can use platform's modal dialogs
     */
    val showDialog: (MosaikDialog) -> Unit,
    val pasteToClipboard: (text: String) -> Unit,
    val openBrowser: (url: String) -> Boolean,
) {
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
                viewTree.getAction(action.onPositiveButtonClicked)?.let { { runAction(it, viewTree) } },
                viewTree.getAction(action.onNegativeButtonClicked)?.let { { runAction(it, viewTree) } }
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
}

data class MosaikDialog(
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String?,
    val positiveButtonClicked: Runnable?,
    val negativeButtonClicked: Runnable?
)