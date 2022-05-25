package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.*


@MosaikDsl
fun ViewContent.showDialog(
    message: String,
    id: String? = null,
    init: (@MosaikDsl DialogAction).() -> Unit = {}
): DialogAction {
    val dialogAction = DialogAction()
    dialogAction.message = message

    return action(dialogAction, id, init)
}

@MosaikDsl
fun ViewContent.openBrowser(
    url: String,
    id: String? = null,
    init: (@MosaikDsl BrowserAction).() -> Unit = {}
): BrowserAction {
    val browserAction = BrowserAction()
    browserAction.url = url
    return action(browserAction, id, init)
}

@MosaikDsl
fun ViewContent.navigateToApp(
    url: String,
    id: String? = null,
    init: (@MosaikDsl NavigateAction).() -> Unit = {}
): NavigateAction {
    val browserAction = NavigateAction()
    browserAction.url = url
    return action(browserAction, id, init)
}

@MosaikDsl
fun ViewContent.invokeErgoPay(
    url: String,
    id: String? = null,
    init: (@MosaikDsl ErgoPayAction).() -> Unit = {}
): ErgoPayAction {
    val browserAction = ErgoPayAction()
    browserAction.url = url
    return action(browserAction, id, init)
}

@MosaikDsl
fun ViewContent.changeView(
    newContent: ViewContent,
    id: String? = null,
    init: (@MosaikDsl ChangeSiteAction).() -> Unit = {}
): ChangeSiteAction {
    val changeSiteAction = ChangeSiteAction()
    changeSiteAction.newContent = newContent
    return action(changeSiteAction, id, init)
}

@MosaikDsl
fun ViewContent.copyToClipboard(
    text: String,
    id: String? = null,
    init: (@MosaikDsl CopyClipboardAction).() -> Unit = {}
): CopyClipboardAction {
    val clipboardAction = CopyClipboardAction()
    clipboardAction.text = text

    return action(clipboardAction, id, init)
}

@MosaikDsl
fun ViewContent.backendRequest(
    url: String,
    id: String? = null,
    init: (@MosaikDsl BackendRequestAction).() -> Unit = {}
): BackendRequestAction {
    val backendRequestAction = BackendRequestAction()
    backendRequestAction.url = url

    return action(backendRequestAction, id, init)
}

@MosaikDsl
fun ViewContent.reloadApp(
    id: String? = null,
    init: (@MosaikDsl ReloadAction).() -> Unit = {}
): ReloadAction {
    val backendRequestAction = ReloadAction()

    return action(backendRequestAction, id, init)
}