package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.DialogAction


@MosaikDsl
fun ViewContent.dialogAction(
    message: String,
    id: String? = null,
    init: (@MosaikDsl DialogAction).() -> Unit = {}
): DialogAction {
    val dialogAction = DialogAction()
    dialogAction.message = message

    return action(dialogAction, id, init)
}

// TODO BackendRequestAction BrowserAction ChangeSiteAction ErgoPayAction
//  NavigateAction CopyClipboardAction