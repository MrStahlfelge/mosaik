package org.ergoplatform.mosaik

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.actions.ChangeSiteAction
import org.ergoplatform.mosaik.model.actions.DialogAction

open class ActionRunner(
    val coroutineScope: () -> CoroutineScope,
    /**
     * Handler to show and manage modal dialogs. These dialogs are managed outside Mosaik's
     * [ViewTree] so that implementations can use platform's modal dialogs
     */
    val dialogHandler: (MosaikDialog) -> Unit
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
                else -> TODO("Action type ${action.javaClass.simpleName} not yet implemented")
            }
        } catch (t: Throwable) {
            MosaikLogger.logError("Error running ${action.javaClass.simpleName}", t)
        }
    }

    private fun runDialogAction(action: DialogAction, viewTree: ViewTree) {
        dialogHandler(
            MosaikDialog(
                action.message,
                action.positiveButtonText ?: "OK",
                action.negativeButtonText,
                action.onPositiveButtonClicked?.let { { runAction(it, viewTree) } },
                action.onNegativeButtonClicked?.let { { runAction(it, viewTree) } }
            )
        )
    }

    private fun runChangeSiteAction(action: ChangeSiteAction, viewTree: ViewTree) {
        try {
            viewTree.setContentView(action.element.id, action.element)
        } catch (nf: ElementNotFoundException) {
            MosaikLogger.logInfo(
                "${action.javaClass.simpleName}: element ${nf.elementId} not found, replacing complete view tree",
                nf
            )
            viewTree.setContentView(null, action.element)
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