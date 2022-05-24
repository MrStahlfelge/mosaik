package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.layout.*

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
fun <G : ViewGroup> G.column(init: (@MosaikDsl Column).() -> Unit): Column =
    viewElement(Column(), init)

@MosaikDsl
fun ViewContent.row(init: (@MosaikDsl Row).() -> Unit): Row =
    viewElement(Row(), init)

@MosaikDsl
fun <G : ViewGroup> G.row(init: (@MosaikDsl Row).() -> Unit): Row =
    viewElement(Row(), init)

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

@MosaikDsl
fun Row.layout(
    vAlignment: VAlignment,
    weight: Int = 0,
    init: (@MosaikDsl ViewGroup).() -> Unit
) {
    val intermediateColumn = object : LayoutingViewGroup<Row>(this) {
        override fun addChild(element: ViewElement) {
            realViewGroup.addChild(element, vAlignment, weight)
        }
    }
    intermediateColumn.init()
}

@MosaikDsl
fun Box.layout(
    hAlignment: HAlignment,
    vAlignment: VAlignment,
    init: (@MosaikDsl ViewGroup).() -> Unit
) {
    val intermediateColumn = object : LayoutingViewGroup<Box>(this) {
        override fun addChild(element: ViewElement) {
            realViewGroup.addChild(element, hAlignment, vAlignment)
        }
    }
    intermediateColumn.init()
}

