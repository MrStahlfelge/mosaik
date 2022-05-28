package org.ergoplatform.mosaik

import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.ergoplatform.mosaik.model.*
import org.ergoplatform.mosaik.model.ui.layout.Box

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
            object : MosaikRuntime(
                backendConnector = object : MosaikBackendConnector {
                    override fun loadMosaikApp(
                        url: String,
                        referrer: String?
                    ): MosaikApp {
                        return MosaikApp().apply {
                            manifest = MosaikManifest(
                                "appname",
                                0,
                                MosaikContext.LIBRARY_MOSAIK_VERSION,
                                null,
                                0,
                            )
                            view = boxRoot
                        }
                    }

                    override fun fetchAction(
                        url: String,
                        baseUrl: String?,
                        values: Map<String, Any?>,
                        referrer: String?
                    ): FetchActionResponse {
                        throw UnsupportedOperationException()
                    }

                    override fun fetchLazyContent(
                        url: String,
                        baseUrl: String?,
                        referrer: String
                    ): ViewContent {
                        throw UnsupportedOperationException()
                    }

                    override fun fetchImage(
                        url: String,
                        baseUrl: String?,
                        referrer: String?
                    ): ByteArray {
                        throw UnsupportedOperationException()
                    }
                }) {
                override val coroutineScope: CoroutineScope
                    get() = GlobalScope

                override fun showDialog(dialog: MosaikDialog) {

                }

                override fun pasteToClipboard(text: String) {

                }

                override fun openBrowser(url: String): Boolean {
                    return true
                }
            }


        runtime.loadMosaikApp("")

        // wait for the coroutine
        Thread.sleep(100)

        return runtime
    }
}