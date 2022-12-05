package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.NotificationCheckResponse
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
    suspend fun fetchImage(
        url: String,
        baseUrl: String?,
        referrer: String?
    ): ByteArray

    /**
     * reports an error to the given url. This method should not throw any exceptions
     * reportUrl is from [MosaikManifest#errorReportUrl]
     */
    fun reportError(reportUrl: String, appUrl: String, t: Throwable)

    /**
     * performs a check for notifications. notificationUrl is from [MosaikManifest]
     */
    fun checkForNotification(notificationUrl: String): NotificationCheckResponse

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