package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.layout.Box
import org.ergoplatform.mosaik.model.ui.layout.Column
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.Padding

@MosaikDsl
fun ViewContent.box(init: (@MosaikDsl Box).() -> Unit): Box = viewElement(Box(), init)

@MosaikDsl
fun <G : ViewGroup> G.box(padding: Padding? = null, init: (@MosaikDsl Box).() -> Unit = {}): Box =
    viewElement(Box().apply {
        padding?.let { this.padding = padding }
    }, init)

@MosaikDsl
fun ViewContent.column(init: (@MosaikDsl Column).() -> Unit): Column =
    viewElement(Column(), init)

@MosaikDsl
private abstract class LayoutingViewGroup<S : ViewGroup>(val realViewGroup: S) : ViewGroup {
    override fun getChildren(): MutableList<ViewElement> {
        return realViewGroup.children
    }

    override fun replaceChild(elementToReplace: ViewElement, newElement: ViewElement) {
        realViewGroup.replaceChild(elementToReplace, newElement)
    }
}

@MosaikDsl
fun Column.layout(
    hAlignment: HAlignment,
    weight: Int = 0,
    init: (@MosaikDsl ViewGroup).() -> Unit
) {
    val intermediateColumn = object : LayoutingViewGroup<Column>(this) {
        override fun addChild(element: ViewElement) {
            realViewGroup.addChild(element, hAlignment, weight)
        }
    }
    intermediateColumn.init()
}

