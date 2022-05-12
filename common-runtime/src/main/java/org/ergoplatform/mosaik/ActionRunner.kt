package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.actions.ChangeSiteAction

open class ActionRunner {
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
            MosaikLogger.logWarning("Error running ${action.javaClass.simpleName}", nf)
        }
    }
}