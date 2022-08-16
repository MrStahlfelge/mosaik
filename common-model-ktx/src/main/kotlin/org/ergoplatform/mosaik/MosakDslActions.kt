package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.*


@MosaikDsl
fun ViewContent.showDialog(
    message: String,
    id: String? = null,
    init: (@MosaikDsl DialogAction).() -> Unit = {}
): DialogAction {
    return addAction(buildDialogAction(message), id, init)
}

@MosaikDsl
fun showDialog(
    message: String,
    id: String? = null,
    init: (@MosaikDsl DialogAction).() -> Unit = {}
): DialogAction {
    return initAction(buildDialogAction(message), id, init)
}

private fun buildDialogAction(message: String): DialogAction {
    val dialogAction = DialogAction()
    dialogAction.message = message
    return dialogAction
}

@MosaikDsl
fun ViewContent.openBrowser(
    url: String,
    id: String? = null,
    init: (@MosaikDsl BrowserAction).() -> Unit = {}
): BrowserAction {
    val browserAction = BrowserAction()
    browserAction.url = url
    return addAction(browserAction, id, init)
}

@MosaikDsl
fun ViewContent.showTokenInformation(
    tokenId: String,
    actionId: String? = null,
    init: (@MosaikDsl TokenInformationAction).() -> Unit = {}
): TokenInformationAction {
    val tokenInformationAction = TokenInformationAction(tokenId)
    return addAction(tokenInformationAction, actionId, init)
}

@MosaikDsl
fun ViewContent.scanQrCode(
    newContent: ViewContent,
    id: String? = null,
    init: (@MosaikDsl QrCodeAction).() -> Unit = {}
): QrCodeAction {
    val qrCodeAction = QrCodeAction()
    qrCodeAction.newContent = newContent
    return addAction(qrCodeAction, id, init)
}

@MosaikDsl
fun ViewContent.navigateToApp(
    url: String,
    id: String? = null,
    init: (@MosaikDsl NavigateAction).() -> Unit = {}
): NavigateAction {
    val browserAction = NavigateAction()
    browserAction.url = url
    return addAction(browserAction, id, init)
}

@MosaikDsl
fun ViewContent.invokeErgoPay(
    url: String,
    onFinishedAction: Action,
    id: String? = null,
    init: (@MosaikDsl ErgoPayAction).() -> Unit = {}
): ErgoPayAction {
    return addAction(buildErgoPayAction(url).apply {
        onFinished = onFinishedAction.id
    }, id, init)
}

@MosaikDsl
fun invokeErgoPay(
    url: String,
    id: String? = null,
    init: (@MosaikDsl ErgoPayAction).() -> Unit = {}
): ErgoPayAction {
    return initAction(buildErgoPayAction(url), id, init)
}

private fun buildErgoPayAction(url: String): ErgoPayAction {
    val ergoPayAction = ErgoPayAction()
    ergoPayAction.url = url
    return ergoPayAction
}

private fun buildErgoAuthAction(url: String): ErgoAuthAction {
    val ergoAuthAction = ErgoAuthAction()
    ergoAuthAction.url = url
    return ergoAuthAction
}

@MosaikDsl
fun ViewContent.invokeErgoAuth(
    url: String,
    onFinishedAction: Action,
    id: String? = null,
    init: (@MosaikDsl ErgoAuthAction).() -> Unit = {}
): ErgoAuthAction {
    return addAction(buildErgoAuthAction(url).apply {
        onFinished = onFinishedAction.id
    }, id, init)
}

@MosaikDsl
fun invokeErgoAuth(
    url: String,
    id: String? = null,
    init: (@MosaikDsl ErgoAuthAction).() -> Unit = {}
): ErgoAuthAction {
    return initAction(buildErgoAuthAction(url), id, init)
}

@MosaikDsl
fun ViewContent.changeView(
    newContent: ViewContent,
    id: String? = null,
    init: (@MosaikDsl ChangeSiteAction).() -> Unit = {}
) = addAction(buildChangeSiteAction(newContent), id, init)


@MosaikDsl
fun changeView(
    newContent: ViewContent,
    id: String? = null,
    init: (@MosaikDsl ChangeSiteAction).() -> Unit = {}
) = initAction(buildChangeSiteAction(newContent), id, init)


private fun buildChangeSiteAction(newContent: ViewContent): ChangeSiteAction {
    val changeSiteAction = ChangeSiteAction()
    changeSiteAction.newContent = newContent
    return changeSiteAction
}

@MosaikDsl
fun ViewContent.copyToClipboard(
    text: String,
    id: String? = null,
    init: (@MosaikDsl CopyClipboardAction).() -> Unit = {}
): CopyClipboardAction {
    val clipboardAction = CopyClipboardAction()
    clipboardAction.text = text

    return addAction(clipboardAction, id, init)
}

@MosaikDsl
fun ViewContent.backendRequest(
    url: String,
    id: String? = null,
    init: (@MosaikDsl BackendRequestAction).() -> Unit = {}
): BackendRequestAction {
    val backendRequestAction = BackendRequestAction()
    backendRequestAction.url = url

    return addAction(backendRequestAction, id, init)
}

@MosaikDsl
fun ViewContent.reloadApp(
    id: String? = null,
    init: (@MosaikDsl ReloadAction).() -> Unit = {}
): ReloadAction {
    val backendRequestAction = ReloadAction()

    return addAction(backendRequestAction, id, init)
}

@MosaikDsl
fun backendResponse(
    appVersion: Int,
    responseAction: Action
) = FetchActionResponse(appVersion, responseAction)