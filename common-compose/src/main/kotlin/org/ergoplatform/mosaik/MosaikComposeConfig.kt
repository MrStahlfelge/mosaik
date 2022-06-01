package org.ergoplatform.mosaik

import androidx.compose.ui.graphics.ImageBitmap

object MosaikComposeConfig {

    /**
     * if set to true, hitting Enter/Return key on non-multiline text input fields will launch
     * imeAction (works only on Desktop, crashes on Android)
     */
    var interceptReturnForImeAction = false

    /**
     * mandatory to set, how to convert a bitmap into a ByteArray
     */
    lateinit var convertByteArrayToImageBitmap: (ByteArray) -> ImageBitmap

    /**
     * set the scroll bar alpha value when no scrolling is performed
     */
    var scrollMinAlpha = .5f
    /**
     * set the scroll bar alpha value when scrolling is performed (works only on Android)
     */
    var scrollMaxAlpha = 1f
}