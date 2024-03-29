package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.ui.LazyLoadBox
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.layout.*

@MosaikDsl
fun ViewContent.box(
    padding: Padding? = Padding.DEFAULT,
    init: (@MosaikDsl Box).() -> Unit
): Box =
    viewElement(Box().apply {
        padding?.let { this.padding = padding }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.box(padding: Padding? = null, init: (@MosaikDsl Box).() -> Unit = {}): Box =
    viewElement(Box().apply {
        padding?.let { this.padding = padding }
    }, init)

@MosaikDsl
fun ViewContent.card(
    outerPadding: Padding? = Padding.DEFAULT,
    init: (@MosaikDsl Card).() -> Unit
): Card = viewElement(Card().apply {
    outerPadding?.let { padding = outerPadding }
}, init)

@MosaikDsl
fun <G : ViewGroup> G.card(
    outerPadding: Padding? = null,
    init: (@MosaikDsl Card).() -> Unit = {}
): Card =
    viewElement(Card().apply {
        outerPadding?.let { padding = outerPadding }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.lazyLoadBox(
    url: String,
    padding: Padding? = null,
    init: (@MosaikDsl LazyLoadBox).() -> Unit = {}
): LazyLoadBox =
    viewElement(LazyLoadBox().apply {
        this.requestUrl = url
        padding?.let { this.padding = padding }
    }, init)

@MosaikDsl
fun ViewContent.column(
    padding: Padding? = Padding.DEFAULT,
    spacing: Padding? = null,
    childAlignment: HAlignment? = null,
    init: (@MosaikDsl Column).() -> Unit
): Column =
    viewElement(Column().apply {
        padding?.let { this.padding = padding }
        spacing?.let { this.spacing = spacing }
        setAlignmentForAdded(childAlignment)
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.column(
    padding: Padding? = null,
    spacing: Padding? = null,
    childAlignment: HAlignment? = null,
    init: (@MosaikDsl Column).() -> Unit
): Column =
    viewElement(Column().apply {
        padding?.let { this.padding = padding }
        spacing?.let { this.spacing = spacing }
        setAlignmentForAdded(childAlignment)
    }, init)

@MosaikDsl
fun ViewContent.grid(
    padding: Padding? = Padding.DEFAULT,
    elementSize: Grid.ElementSize = Grid.ElementSize.SMALL,
    init: (@MosaikDsl Grid).() -> Unit
): Grid =
    viewElement(Grid().apply {
        padding?.let { this.padding = padding }
        this.elementSize = elementSize
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.grid(
    padding: Padding? = null,
    elementSize: Grid.ElementSize = Grid.ElementSize.SMALL,
    init: (@MosaikDsl Grid).() -> Unit
): Grid =
    viewElement(Grid().apply {
        padding?.let { this.padding = padding }
        this.elementSize = elementSize
    }, init)

@MosaikDsl
fun ViewContent.row(
    padding: Padding? = Padding.DEFAULT,
    spacing: Padding? = null,
    childAlignment: VAlignment? = null,
    init: (@MosaikDsl Row).() -> Unit
): Row =
    viewElement(Row().apply {
        padding?.let { this.padding = padding }
        spacing?.let { this.spacing = spacing }
        setAlignmentForAdded(childAlignment)
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.row(
    padding: Padding? = null,
    spacing: Padding? = null,
    childAlignment: VAlignment? = null,
    packed: Boolean? = false,
    init: (@MosaikDsl Row).() -> Unit
): Row =
    viewElement(Row().apply {
        padding?.let { this.padding = padding }
        spacing?.let { this.spacing = spacing }
        setAlignmentForAdded(childAlignment)
        packed?.let { this.isPacked = packed }
    }, init)

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
    vAlignment: VAlignment = VAlignment.CENTER,
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
    hAlignment: HAlignment = HAlignment.CENTER,
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

