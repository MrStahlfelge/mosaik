package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.ViewElement

fun ViewElement.onClickAction(a: Action) {
    this.onClickAction = a.id
}

fun ViewElement.onClickAction(id: String) {
    this.onClickAction = id
}