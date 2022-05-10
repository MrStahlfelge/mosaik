package org.ergoplatform.mosaik

import junit.framework.TestCase
import org.ergoplatform.mosaik.model.ui.layout.Box

class ViewTreeTest : TestCase() {

    fun testReplaceElement() {
        val viewTree = buildViewTree()
        val oldIdsList = getIdsList(viewTree)
        viewTree.setContentView("AA", Box().apply { id = "aa" })
        val idList = getIdsList(viewTree)

        assertEquals(oldIdsList.size - 1, idList.size)
        assertTrue(idList.contains("aa"))
        assertFalse(idList.contains("AA"))
        assertFalse(idList.contains("AAA"))
    }

    fun testVisitAllElements() {
        val viewTree = buildViewTree()

        val idList = getIdsList(viewTree)

        assertEquals(5, idList.size)
        assertEquals(idList.toMutableList().apply { sortBy { it.length } }, idList)
    }

    private fun getIdsList(viewTree: ViewTree): ArrayList<String> {
        val idList = ArrayList<String>()
        viewTree.visitAllElements { idList.add(it.id ?: "") }
        return idList
    }

    private fun buildViewTree(): ViewTree {
        val boxRoot = Box()
        val boxA = Box().apply { id = "A" }
        val boxAA = Box().apply { id = "AA" }
        val boxAAA = Box().apply { id = "AAA" }
        val boxB = Box().apply { id = "B" }

        boxRoot.addChild(boxA)
        boxA.addChild(boxAA)
        boxAA.addChild(boxAAA)
        boxRoot.addChild(boxB)

        val viewTree = ViewTree("guid")
        viewTree.setRootView(boxRoot, 0)
        return viewTree
    }
}