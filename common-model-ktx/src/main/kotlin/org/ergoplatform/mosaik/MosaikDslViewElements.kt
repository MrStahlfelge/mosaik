package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.Image
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label

@MosaikDsl
fun <G : ViewGroup> G.label(text: String, init: (@MosaikDsl Label).() -> Unit = {}): Label =
    viewElement(Label().apply {
        this.text = text
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.button(text: String, init: (@MosaikDsl Button).() -> Unit = {}): Button =
    viewElement(Button().apply {
        this.text = text
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.image(url: String, init: (@MosaikDsl Image).() -> Unit = {}): Image =
    viewElement(Image().apply {
        this.url = url
    }, init)

