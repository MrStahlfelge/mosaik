package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.*
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

@MosaikDsl
fun <G : ViewGroup> G.label(
    text: String,
    style: LabelStyle? = null,
    textAlignment: HAlignment? = null,
    textColor: ForegroundColor? = null,
    init: (@MosaikDsl Label).() -> Unit = {}
): Label =
    viewElement(Label().apply {
        this.text = text
        style?.let { this.style = style }
        textAlignment?.let { this.textAlignment = textAlignment }
        textColor?.let { this.textColor = textColor }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.button(
    text: String,
    style: Button.ButtonStyle? = null,
    init: (@MosaikDsl Button).() -> Unit = {}
): Button =
    viewElement(Button().apply {
        this.text = text
        style?.let { this.style = style }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.image(
    url: String,
    size: Image.Size? = null,
    init: (@MosaikDsl Image).() -> Unit = {}
): Image =
    viewElement(Image().apply {
        this.url = url
        size?.let { this.size = size }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.icon(
    iconType: IconType,
    size: Icon.Size? = null,
    tintColor: ForegroundColor? = null,
    init: (@MosaikDsl Icon).() -> Unit = {}
): Icon =
    viewElement(Icon().apply {
        this.iconType = iconType
        size?.let { this.iconSize = size }
        tintColor?.let { this.tintColor = tintColor }
    }, init)

