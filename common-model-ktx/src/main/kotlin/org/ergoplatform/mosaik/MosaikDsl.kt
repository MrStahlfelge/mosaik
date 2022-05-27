package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.layout.Box
import java.util.*

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class MosaikDsl

@MosaikDsl
fun mosaikApp(
    appName: String, appVersion: Int,
    appDescription: String? = null,
    appIconUrl: String? = null,
    appBaseUrl: String? = null,
    targetMosaikVersion: Int = MosaikContext.LIBRARY_MOSAIK_VERSION,
    targetCanvasDimension: MosaikManifest.CanvasDimension? = null,
    cacheLifetime: Int = 0,
    errorReportUrl: String? = null,
    init: (MosaikApp).() -> Unit
): MosaikApp {
    val appInfo = MosaikApp()

    appInfo.manifest = MosaikManifest(
        appName,
        appVersion,
        targetMosaikVersion,
        targetCanvasDimension,
        cacheLifetime
    ).apply {
        this.appDescription = appDescription
        this.iconUrl = appIconUrl
        this.baseUrl = appBaseUrl
        this.errorReportUrl = errorReportUrl
    }

    appInfo.view = Box()

    appInfo.init()
    return appInfo
}

@MosaikDsl
fun mosaikView(
    init: (ViewContent).() -> Unit
): ViewContent {
    val viewContent = ViewContent()

    viewContent.init()
    return viewContent
}

@MosaikDsl
fun <A : Action> ViewContent.addAction(
    action: A,
    id: String? = null,
    init: (@MosaikDsl A).() -> Unit = {},
    setDefaultId: Boolean = id == null,
): A {
    initAction(action, id, init, setDefaultId)

    // add the action to the view content, if there is no equal one
    val currentActions = actions
    if (currentActions.none { it == action }) {
        currentActions.add(action)
        actions = currentActions
    }

    return action
}

@MosaikDsl
fun <A : Action> initAction(
    action: A,
    id: String? = null,
    init: (@MosaikDsl A).() -> Unit = {},
    setDefaultId: Boolean = id == null,
): A {
    if (setDefaultId) {
        action.id = UUID.randomUUID().toString()
    } else {
        id?.let { action.id = id }
    }

    // and call the init with ViewContent as receiver
    action.init()

    return action
}

@MosaikDsl
fun <V : ViewElement> ViewContent.viewElement(
    viewElement: V,
    init: (@MosaikDsl V).() -> Unit = {}
): V {
    view = viewElement
    viewElement.init()
    return viewElement
}

@MosaikDsl
fun <G : ViewGroup, V : ViewElement> G.viewElement(
    viewElement: V,
    init: (@MosaikDsl V).() -> Unit = {}
): V {
    addChild(viewElement)
    viewElement.init()
    return viewElement
}
