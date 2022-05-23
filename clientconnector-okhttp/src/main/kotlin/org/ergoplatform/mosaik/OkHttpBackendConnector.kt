package org.ergoplatform.mosaik

import okhttp3.*
import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.InitialAppInfo
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.serialization.MosaikSerializer
import java.io.IOException
import java.util.concurrent.TimeUnit

open class OkHttpBackendConnector(
    okClientBuilder: OkHttpClient.Builder,
    timeoutSeconds: Long = 30
) : MosaikBackendConnector {

    private val serializer = MosaikSerializer()
    private val okClient = okClientBuilder.connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .writeTimeout(timeoutSeconds, TimeUnit.SECONDS).build()

    override fun loadMosaikApp(
        url: String,
        context: MosaikContext
    ): InitialAppInfo {
        val (contentType, json) = fetchHttpGetStringSync(
            url,
            Headers.of(serializer.contextHeadersMap(context))
        )
        if (contentType?.lowercase()?.startsWith("text/html") == true) {
            // we could have a website here, check for <link rel="mosaik" tag>
            // this is poor man's xml parser, but on Android real XML parsing is a hard task
            val mosaikRelLink = checkForMosaikRelTag(json)
            if (mosaikRelLink != null) {
                // found a mosaik rel tag, so let's load from there
                return loadMosaikApp(makeAbsoluteUrl(url, mosaikRelLink), context)
            }
        }
        return serializer.firstRequestResponseFromJson(json)
    }

    internal fun checkForMosaikRelTag(html: String): String? {
        val searchNext = { startIndex: Int -> html.indexOf("<link ", startIndex, true) }
        var startIndex = searchNext(0)
        while (startIndex >= 0) {
            val endIndex = html.indexOf('>', startIndex, true) + 1

            val linkElement = html.substring(startIndex, endIndex)

            if (linkElement.contains("rel=\"mosaik\"", true)) {
                // we have found a mosaik element, extract its href
                val startHrefTxt = "href=\""
                val startHrefPos = linkElement.indexOf(startHrefTxt, 0, true)
                if (startHrefPos >= 0) {
                    val endHrefPos =
                        linkElement.indexOf('\"', startHrefPos + startHrefTxt.length + 1)
                    if (endHrefPos > 0) {
                        return linkElement.substring(startHrefPos + startHrefTxt.length, endHrefPos)
                    }
                }
            }

            startIndex = searchNext(50)
        }
        return null
    }

    override fun fetchAction(
        url: String,
        baseUrl: String?,
        context: MosaikContext,
        values: Map<String, Any?>
    ): FetchActionResponse {
        val json = httpPostStringSync(
            makeAbsoluteUrl(baseUrl, url),
            serializer.valuesMapToJson(values),
            Headers.of(serializer.contextHeadersMap(context))
        )
        return serializer.fetchActionResponseFromJson(json)
    }

    private fun makeAbsoluteUrl(baseUrl: String?, url: String): String {
        val loadUrl = if (baseUrl == null || url.contains("://")) url
        else baseUrl.trimEnd('/') + "/" + url.trimStart('/')
        return loadUrl
    }

    override fun fetchImage(url: String, baseUrl: String?): ByteArray =
        fetchHttpGetBytes(makeAbsoluteUrl(baseUrl, url))

    private fun fetchHttpGetBytes(url: String): ByteArray {
        val request = Request.Builder()
            .url(url)
            .build()
        okClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body()!!.bytes()
        }
    }

    private fun fetchHttpGetStringSync(httpUrl: String, headers: Headers): Pair<String?, String> {
        val request = Request.Builder().url(httpUrl).headers(headers).build()
        val serverResponse: Pair<String?, String> =
            okClient.newCall(request).execute()
                .use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected response code $response")
                    }

                    Pair(response.header("content-type"), response.body()!!.string())

                }
        return serverResponse
    }

    private fun httpPostStringSync(httpUrl: String, body: String, headers: Headers): String {
        val request = Request.Builder()
            .url(httpUrl)
            .headers(headers)
            .post(RequestBody.create(MediaType.parse(MEDIA_TYPE_JSON), body))
            .build()

        return okClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("$httpUrl returned $response")
            }

            response.body()!!.string()
        }
    }

    private val MEDIA_TYPE_JSON = "application/json; charset=utf-8"
}