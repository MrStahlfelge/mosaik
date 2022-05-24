package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.InitialAppInfo
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.layout.Box

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class MosaikDsl

@MosaikDsl
fun mosaikApp(
    appName: String, appVersion: Int,
    appDescription: String? = null,
    appIconUrl: String? = null,
    appBaseUrl: String? = null,
    targetMosaikVersion: Int,
    targetCanvasDimension: MosaikManifest.CanvasDimension? = null,
    cacheLifetime: Int = 0,
    errorReportUrl: String? = null,
    init: (@MosaikDsl InitialAppInfo).() -> Unit
): InitialAppInfo {
    val appInfo = InitialAppInfo()

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
fun <A : Action> ViewContent.action(
    action: A,
    id: String? = null,
    init: (@MosaikDsl A).() -> Unit = {},
    setDefaultId: Boolean = id == null,
): A {
    if (setDefaultId) {
        action.id = action.javaClass.simpleName + actions.size
    }

    // add the action to the view content
    val currentActions = actions
    currentActions.add(action)
    actions = currentActions

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
