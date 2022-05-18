package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.Action

/**
 * Handles connection from Mosaik executor app its backend, the dApp
 */
interface MosaikBackendConnector {
    /**
     * first load of a Mosaik app. Blocking, call on a background thread
     */
    fun loadMosaikApp(url: String, context: MosaikContext): Pair<MosaikManifest, ViewContent>

    /**
     * loads an action from Mosaik app. Blocking, call on a background thread
     */
    fun fetchAction(url: String, context: MosaikContext, values: Map<String, Any?>): Pair<Int, Action>
}