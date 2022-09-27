package org.ergoplatform.mosaik

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import org.ergoplatform.mosaik.model.ui.text.TokenLabel

object MosaikComposeConfig {

    /**
     * if set to true, hitting Enter/Return key on non-multiline text input fields will launch
     * imeAction (works only on Desktop, crashes on Android)
     */
    var interceptReturnForImeAction = false

    /**
     * mandatory to set, how to convert a bitmap from a ByteArray
     */
    lateinit var convertByteArrayToImageBitmap: (ByteArray, pixelSize: Int) -> ImageBitmap

    /**
     * need to be set for [QrCode] to display
     */
    var convertQrCodeContentToImageBitmap: ((String) -> ImageBitmap?)? = null

    /**
     * size to display the image element containing the qr code
     */
    var qrCodeSize = 400.dp

    /**
     * set the scroll bar alpha value when no scrolling is performed
     */
    var scrollMinAlpha = .5f

    /**
     * set the scroll bar alpha value when scrolling is performed (works only on Android)
     */
    var scrollMaxAlpha = 1f

    /**
     * if true, text input field contents are preselected when the textfield is editable.
     */
    var preselectEditableInputs = true

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

    var VerticalScrollbar: @Composable (BoxScope.(ScrollState) -> Unit)? = null

    var TokenLabel: @Composable (
        properties: TokenLabel,
        modifier: Modifier,
        content: @Composable (tokenName: String, decimals: Int, modifier: Modifier) -> Unit
    ) -> Unit =
        // default is some kind of no-op implementation
        { properties, modifier, content ->
            content(properties.tokenName ?: properties.tokenId, properties.decimals, modifier)
        }
}