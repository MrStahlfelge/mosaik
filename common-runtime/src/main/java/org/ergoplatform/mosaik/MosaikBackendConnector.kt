package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.InitialAppInfo
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.actions.Action

/**
 * Handles connection from Mosaik executor app its backend, the dApp
 */
interface MosaikBackendConnector {
    /**
     * first load of a Mosaik app. Blocking, call on a background thread
     */
    fun loadMosaikApp(url: String, context: MosaikContext): InitialAppInfo

    /**
     * loads an action from Mosaik app. Blocking, call on a background thread
     */
    fun fetchAction(url: String, baseUrl: String?, context: MosaikContext, values: Map<String, Any?>): FetchActionResponse
}