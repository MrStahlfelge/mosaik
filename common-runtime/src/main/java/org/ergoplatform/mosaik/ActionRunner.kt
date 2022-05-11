package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.actions.ChangeSiteAction

open class ActionRunner {
    open fun runAction(action: Action, viewTree: ViewTree) {
        when (action) {
            is ChangeSiteAction -> {
                runChangeSiteAction(action, viewTree)
            }
            else -> TODO("Action type ${action.javaClass.simpleName} not yet implemented")
        }
    }

    private fun runChangeSiteAction(action: ChangeSiteAction, viewTree: ViewTree) {
        viewTree.setContentView(action.element.id, action.element)
    }
}