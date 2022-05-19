package org.ergoplatform.mosaik

import okhttp3.*
import org.ergoplatform.mosaik.model.*
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
        val json = fetchHttpGetStringSync(url, Headers.of(serializer.contextHeadersMap(context)))
        return serializer.firstRequestResponseFromJson(json)
    }

    override fun fetchAction(
        url: String,
        context: MosaikContext,
        values: Map<String, Any?>
    ): FetchActionResponse {
        val json = httpPostStringSync(
            url,
            serializer.valuesMapToJson(values),
            Headers.of(serializer.contextHeadersMap(context))
        )
        return serializer.fetchActionResponseFromJson(json)
    }

    private fun fetchHttpGetStringSync(httpUrl: String, headers: Headers): String {
        val request = Request.Builder().url(httpUrl).headers(headers).build()
        val stringResponse =
            okClient.newCall(request).execute()
                .use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected response code $response")
                    }

                    response.body()!!.string()

                }
        return stringResponse
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