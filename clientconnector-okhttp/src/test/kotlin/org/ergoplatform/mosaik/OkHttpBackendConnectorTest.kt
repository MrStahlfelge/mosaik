package org.ergoplatform.mosaik

import okhttp3.OkHttpClient
import org.ergoplatform.mosaik.model.MosaikContext
import org.junit.Assert.*

import org.junit.Test

class OkHttpBackendConnectorTest {

    @Test
    fun checkForMosaikRelTag() {
        val backendConnector = OkHttpBackendConnector(
            OkHttpClient.Builder(),
            {
                MosaikContext(
                    MosaikContext.LIBRARY_MOSAIK_VERSION,
                    "",
                    "",
                    "",
                    "",
                    MosaikContext.Platform.DESKTOP
                )
            })

        assertEquals(
            "visitors/",
            backendConnector.checkForMosaikRelTag("<head><link rel=\"mosaik\" href=\"visitors/\"></head>")
        )
        assertNull(backendConnector.checkForMosaikRelTag("<head><link rel=\"mossaik\" href=\"visitors/\"></head>"))
        assertNull(backendConnector.checkForMosaikRelTag("<head><link rel=\"mosaik\" href=\"\"></head>"))
        assertNull(backendConnector.checkForMosaikRelTag("<head><link rel=\"mosaik\"></head>"))
        assertNull(
            backendConnector.checkForMosaikRelTag(
                "<head>"
                        + List<Char>(100) { ' ' }.joinToString()
                        + "<link rel=\"mosaik\"></head>"
            )
        )
        assertNull(backendConnector.checkForMosaikRelTag("<head></head>"))
        assertEquals(
            "_",
            backendConnector.checkForMosaikRelTag("<head><link rel=\"mosaik\" href=\"_\"></head>")
        )
    }
}