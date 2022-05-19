package org.ergoplatform.mosaik

import junit.framework.TestCase
import kotlinx.coroutines.GlobalScope
import org.ergoplatform.mosaik.model.*
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.layout.Box
import java.util.*

class ViewTreeTest : TestCase() {

    fun testReplaceElement() {
        val viewTree = buildRuntime().viewTree
        val oldIdsList = getIdsList(viewTree)
        viewTree.setContentView("AA", ViewContent(Box().apply { id = "aa" }))
        val idList = getIdsList(viewTree)

        assertEquals(oldIdsList.size - 1, idList.size)
        assertTrue(idList.contains("aa"))
        assertFalse(idList.contains("AA"))
        assertFalse(idList.contains("AAA"))
    }

    fun testVisitAllElements() {
        val viewTree = buildRuntime().viewTree

        val idList = getIdsList(viewTree)

        assertEquals(5, idList.size)
        assertEquals(idList.toMutableList().apply { sortBy { it.length } }, idList)
    }

    private fun getIdsList(viewTree: ViewTree): ArrayList<String> {
        val idList = ArrayList<String>()
        viewTree.visitAllElements { idList.add(it.id ?: "") }
        return idList
    }

    private fun buildRuntime(): MosaikRuntime {
        val boxRoot = Box()
        val boxA = Box().apply { id = "A" }
        val boxAA = Box().apply { id = "AA" }
        val boxAAA = Box().apply { id = "AAA" }
        val boxB = Box().apply { id = "B" }

        boxRoot.addChild(boxA)
        boxA.addChild(boxAA)
        boxAA.addChild(boxAAA)
        boxRoot.addChild(boxB)

        val runtime =
            MosaikRuntime(
                coroutineScope = { GlobalScope },
                backendConnector = object : MosaikBackendConnector {
                    override fun loadMosaikApp(
                        url: String,
                        context: MosaikContext
                    ): InitialAppInfo {
                        return InitialAppInfo().apply {
                            manifest = MosaikManifest(
                                "appname",
                                0,
                                0,
                                null,
                                0,
                                0,
                            )
                            view = boxRoot
                        }
                    }

                    override fun fetchAction(
                        url: String,
                        context: MosaikContext,
                        values: Map<String, Any?>
                    ): FetchActionResponse {
                        TODO("Not yet implemented")
                    }

                },
                showDialog = { },
                pasteToClipboard = {},
                openBrowser = { true },
                mosaikContext = MosaikContext(
                    0,
                    UUID.randomUUID().toString(),
                    "",
                    "Testexecutor",
                    "0",
                    MosaikContext.Platform.DESKTOP
                )
            )

        runtime.loadMosaikApp("")

        // wait for the coroutine
        Thread.sleep(100)

        return runtime
    }
}