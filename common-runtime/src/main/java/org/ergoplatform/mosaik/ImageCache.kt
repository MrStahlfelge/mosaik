package org.ergoplatform.mosaik

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.lang.ref.SoftReference

/**
 * Image Cache caches images for [maxCacheSizeBytes] at max, or for the last viewed content root.
 *
 * It prevents multiple downloads of the same image on one screen, or on following screens.
 */
class ImageCache(
    private val backendConnector: MosaikBackendConnector,
    private val maxCacheSizeBytes: Long,
) {

    private val cacheMap = HashMap<String, ImageCacheElement>()
    private val softCacheMap = HashMap<String, SoftReference<ImageCacheElement>>()
    private var lastRootElementContentVersion = 0

    private data class ImageCacheElement(
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

        // make sure to remove cancelled or failed entries
        pruneCache(currentScreenContentVersion)

        val (cacheEntry, hadEntryBefore) = synchronized(cacheMap) {
            val existingImageCacheEntry = cacheMap[absoluteUrl] ?: softCacheMap[absoluteUrl]?.get()

            // this will pick up entries that still downloading or failed to download as well
            // this is intended as a single picture can be used multiple times on the same page
            // these entries are removed in pruneCache() method
            val hadEntryBefore = existingImageCacheEntry != null

            val cacheEntry = existingImageCacheEntry ?: ImageCacheElement(
                absoluteUrl,
                currentScreenContentVersion,
                null
            )

            cacheEntry.lastAccessed = currentScreenContentVersion

            if (!isDynamicUrl)
                cacheMap[absoluteUrl] = cacheEntry

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
            MosaikLogger.logDebug("Image $url served from cache.")

            cacheEntry.content ?: ByteArray(0)
        }
    }

    fun pruneCache(currentScreenContentVersion: Int) {
        if (lastRootElementContentVersion == currentScreenContentVersion)
            return

        lastRootElementContentVersion = currentScreenContentVersion

        synchronized(cacheMap) {
            MosaikLogger.logDebug("Cleaning image cache with ${cacheMap.size} elements")

            val elements = cacheMap.values.toList()

                // delete entries with size 0 (could be because of connection issues)
                // keep the ones from this content version, could be still ongoing downloads
                .filterNot {
                    (it.content?.isEmpty() ?: true) && it.lastAccessed < currentScreenContentVersion
                }

                // leave all images that are loaded since last root element content version
                // for all others: restrict to maxCacheSizeBytes, order by size

                .sortedBy { it.content?.size }

            cacheMap.clear()
            var currentSize = 0

            elements.forEach {
                val sizeWithImage = currentSize + (it.content?.size ?: 0)
                if (sizeWithImage <= maxCacheSizeBytes || it.lastAccessed >= currentScreenContentVersion) {
                    currentSize = sizeWithImage
                    cacheMap[it.url] = it
                } else if (it.content?.isNotEmpty() == true) {
                    // store in soft cache map
                    softCacheMap[it.url] = SoftReference(it)
                }
            }

            MosaikLogger.logDebug("Kept ${cacheMap.size} images with $currentSize bytes")
            MosaikLogger.logDebug("Soft-kept images with ${softCacheMap.values.sumOf { it.get()?.content?.size ?: 0 }} bytes")
        }
    }
}