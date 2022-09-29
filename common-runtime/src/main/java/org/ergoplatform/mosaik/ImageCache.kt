package org.ergoplatform.mosaik

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Image Cache caches images for [maxCacheSizeBytes] at max, or for the last viewed content root.
 *
 * It prevents multiple downloads of the same image on one screen, or on following screens.
 */
class ImageCache(
    private val backendConnector: MosaikBackendConnector,
    private val maxCacheSizeBytes: Long,
) {

    private val cacheMap = HashMap<String, ImageCache>()
    private var lastRootElementContentVersion = 0

    private data class ImageCache(
        val url: String,
        var lastAccessed: Int,
        var content: ByteArray?
    )

    /**
     * checks if an image is in cache and returns the image
     * if the image is currently loading, waits for the image being loaded and returns the image
     * otherwise downloads the image
     */
    suspend fun getImage(
        url: String,
        appUrl: String?,
        currentScreenContentVersion: Int,
        scope: CoroutineScope
    ): ByteArray {
        val absoluteUrl = backendConnector.getAbsoluteUrl(appUrl, url)

        val isDynamicUrl = backendConnector.isDynamicImageUrl(absoluteUrl)

        val (cacheEntry, hadEntryBefore) = synchronized(cacheMap) {
            val existingImageCacheEntry = cacheMap[absoluteUrl]

            val hadEntryBefore = existingImageCacheEntry != null &&
                    existingImageCacheEntry.content?.isEmpty() == false

            val cacheEntry = existingImageCacheEntry ?: ImageCache(
                absoluteUrl,
                currentScreenContentVersion,
                null
            )

            cacheEntry.lastAccessed = currentScreenContentVersion

            if (!hadEntryBefore) {
                cacheEntry.content = null
                cacheMap[absoluteUrl] = cacheEntry
            }

            Pair(cacheEntry, hadEntryBefore)
        }

        return if (!hadEntryBefore || isDynamicUrl) {
            // there is no queued image download yet, start the download
            MosaikLogger.logDebug("Downloading image $url...")

            val byteArray = try {
                backendConnector.fetchImage(url, appUrl, appUrl)
            } catch (t: Throwable) {
                MosaikLogger.logWarning("Could not download image $url", t)
                ByteArray(0)
            }

            cacheEntry.content = byteArray

            byteArray
        } else {
            // wait until download is done, will immediately return if it already is present
            while (cacheEntry.content == null && scope.isActive) {
                delay(50)
            }

            cacheEntry.content ?: ByteArray(0)
        }
    }

    fun pruneCache(currentScreenContentVersion: Int) {
        if (lastRootElementContentVersion == currentScreenContentVersion)
            return

        val keepAllElementsFrom = lastRootElementContentVersion
        lastRootElementContentVersion = currentScreenContentVersion

        synchronized(cacheMap) {
            MosaikLogger.logDebug("Cleaning image cache with ${cacheMap.size} elements")

            val elements = cacheMap.values.toList()

                // delete entries with size 0 (could be because of connection issues)
                .filterNot { it.content?.isEmpty() ?: true }

                // leave all images that are loaded since last root element content version
                // for all others: restrict to 1 mb, order by size

                .sortedBy { it.content?.size }

            cacheMap.clear()
            var currentSize = 0

            elements.forEach {
                val sizeWithImage = currentSize + (it.content?.size ?: 0)
                if (sizeWithImage <= maxCacheSizeBytes || it.lastAccessed >= keepAllElementsFrom) {
                    currentSize = sizeWithImage
                    cacheMap[it.url] = it
                }
            }

            MosaikLogger.logDebug("Kept ${cacheMap.size} images with $currentSize bytes")
        }
    }
}