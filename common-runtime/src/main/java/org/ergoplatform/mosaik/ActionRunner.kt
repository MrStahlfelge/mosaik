package org.ergoplatform.mosaik

import kotlinx.coroutines.CoroutineScope
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.actions.ChangeSiteAction

open class ActionRunner(val coroutineScope: () -> CoroutineScope) {
    open fun runAction(action: Action, viewTree: ViewTree) {
        try {
            when (action) {
                is ChangeSiteAction -> {
                    runChangeSiteAction(action, viewTree)
                }
                else -> TODO("Action type ${action.javaClass.simpleName} not yet implemented")
            }
        } catch (t: Throwable) {
            MosaikLogger.logError("Error running ${action.javaClass.simpleName}", t)
        }
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