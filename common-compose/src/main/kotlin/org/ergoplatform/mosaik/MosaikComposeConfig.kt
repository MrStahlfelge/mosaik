package org.ergoplatform.mosaik

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    // workaround for Compose Compatibiliy between Android and Desktop
    // just route to Compose implementations in platform code

    lateinit var DropDownMenu: @Composable (
        expanded: Boolean,
        dismiss: () -> Unit,
        modifier: Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) -> Unit

    lateinit var DropDownItem: @Composable (
        onClick: () -> Unit,
        content: @Composable RowScope.() -> Unit
    ) -> Unit
}