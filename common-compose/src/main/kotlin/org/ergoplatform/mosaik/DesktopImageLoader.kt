package org.ergoplatform.mosaik

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.*

object DesktopImageLoader {
    fun loadAndScaleImage(imageBytes: ByteArray, pixelSize: Int): ImageBitmap {
        val skiaImage = Image.makeFromEncoded(imageBytes.inputStream().readAllBytes())
        val imageInfo = skiaImage.imageInfo
        val imageToRender =
            if (imageInfo.width > pixelSize || imageInfo.height > pixelSize) {
                val pixMap = Pixmap.make(
                    ImageInfo(
                        pixelSize,
                        pixelSize,
                        imageInfo.colorType,
                        imageInfo.colorAlphaType,
                        imageInfo.colorSpace
                    ),
                    Data.makeUninitialized(imageInfo.width * imageInfo.height * imageInfo.bytesPerPixel),
                    imageInfo.width * imageInfo.bytesPerPixel
                )
                val scaled = skiaImage.scalePixels(pixMap, SamplingMode.LINEAR, false)
                if (scaled) Image.makeFromPixmap(pixMap) else skiaImage
            } else skiaImage
        return imageToRender.toComposeImageBitmap()
    }
}