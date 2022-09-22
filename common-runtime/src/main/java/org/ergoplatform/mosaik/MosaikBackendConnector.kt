package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.ViewContent

/**
 * Handles connection from Mosaik executor app its backend, the dApp
 */
interface MosaikBackendConnector {
    /**
     * first load of a Mosaik app. Blocking, call on a background thread
     * For connection related problems, [ConnectionException] should be thrown
     */
    fun loadMosaikApp(
        url: String,
        referrer: String?,
    ): AppLoaded

    /**
     * loads an action from Mosaik app. Blocking, call on a background thread
     * For connection related problems, [ConnectionException] should be thrown
     */
    fun fetchAction(
        url: String, baseUrl: String?,
        values: Map<String, Any?>,
        referrer: String?,
    ): FetchActionResponse

    /**
     * loads contents of a LazyLoadBox. Blocking, call on a background thread
     */
    fun fetchLazyContent(
        url: String,
        baseUrl: String?,
        referrer: String
    ): ViewContent

    /**
     * loads an Image. Blocking, call on a background thread
     */
    fun fetchImage(
        url: String,
        baseUrl: String?,
        referrer: String?
    ): ByteArray

    /**
     * reports an error to the given url
     */
    fun reportError(reportUrl: String, appUrl: String, t: Throwable)

    /**
     * returns the absolute url for an app resource, resolves any relative links
     * defaults to the original, unaltered url
     */
    fun getAbsoluteUrl(appUrl: String?, url: String): String

    /**
     * returns true if an image is, based on its URL, dynamic and should not be cached
     */
    fun isDynamicImageUrl(absoluteUrl: String): Boolean

    data class AppLoaded(val mosaikApp: MosaikApp, val appUrl: String)
}