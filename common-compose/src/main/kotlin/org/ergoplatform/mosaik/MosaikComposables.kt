package org.ergoplatform.mosaik

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ergoplatform.mosaik.MosaikStyleConfig.defaultLabelColor
import org.ergoplatform.mosaik.MosaikStyleConfig.primaryButtonTextColor
import org.ergoplatform.mosaik.MosaikStyleConfig.primaryLabelColor
import org.ergoplatform.mosaik.MosaikStyleConfig.secondaryButtonColor
import org.ergoplatform.mosaik.MosaikStyleConfig.secondaryButtonTextColor
import org.ergoplatform.mosaik.MosaikStyleConfig.secondaryLabelColor
import org.ergoplatform.mosaik.MosaikStyleConfig.textButtonColorDisabled
import org.ergoplatform.mosaik.MosaikStyleConfig.textButtonTextColor
import org.ergoplatform.mosaik.model.ui.*
import org.ergoplatform.mosaik.model.ui.input.TextInputField
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.ergoplatform.mosaik.model.ui.text.TruncationType

@Composable
fun MosaikViewTree(viewTree: ViewTree, modifier: Modifier = Modifier) {
    val modification by viewTree.contentState.collectAsState()
    val locked by viewTree.uiLockedState.collectAsState()

    Box(modifier) {
        modification.second?.let { viewTreeRoot ->
            // Crossfade animation here caused some elements to not update
            MosaikTreeElement(
                viewTreeRoot, Modifier.fillMaxWidth().alpha(if (locked) .3f else 1f)
                    .verticalScroll(rememberScrollState()).padding(Padding.DEFAULT.toCompose())
            )
        }
        if (locked) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)))
            CircularProgressIndicator(
                Modifier.size(48.dp).align(Alignment.Center),
                color = primaryLabelColor
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MosaikTreeElement(treeElement: TreeElement, modifier: Modifier = Modifier) {
    val element = treeElement.element

    val newModifier = if (element is LayoutElement) {
        modifier.padding(element.padding.toCompose())
    } else {
        modifier
    }.alpha(if (element.isVisible) 1.0f else 0.0f)
        .then(
            if (treeElement.respondsToClick)
                Modifier.combinedClickable(
                    onClick = treeElement::clicked,
                    onLongClick = treeElement::longPressed,
                ) else Modifier
        )

    when (element) {
        is Box -> {
            MosaikBox(newModifier, treeElement)
        }
        is Label -> {
            MosaikLabel(treeElement, newModifier)
        }
        is Column -> {
            MosaikColumn(newModifier, treeElement)
        }
        is Row -> {
            MosaikRow(newModifier, treeElement)
        }
        is Button -> {
            MosaikButton(treeElement, newModifier)
        }
        is TextInputField -> {
            MosaikTextInputField(treeElement, newModifier)
        }
        is LoadingIndicator -> {
            MosaikLoadingIndicator(treeElement, newModifier)
        }
        is LazyLoadBox -> {
            TODO()
        }
        is Icon -> {
            MosaikIcon(treeElement, newModifier)
        }
        is Image -> {
            MosaikImage(treeElement, newModifier)
        }
        else -> {
            throw IllegalArgumentException("Unsupported view element: ${element.javaClass.simpleName}")
        }
    }
}

@Composable
fun MosaikImage(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as Image
    val imageBytes = treeElement.getResourceBytes
    val modifierWithSize = modifier.size(
        when (element.size) {
            Image.Size.SMALL -> 62.dp
            Image.Size.MEDIUM -> 124.dp
            Image.Size.LARGE -> 258.dp
        }
    )

    // imageBytes null -> still downloading
    // imageBytes length 0 -> error

    val imageBitmap = remember(imageBytes) {
        if (imageBytes != null && imageBytes.isNotEmpty()) {

            try {
                loadImageBitmap(imageBytes.inputStream())
            } catch (t: Throwable) {
                MosaikLogger.logError("Could not load bitmap", t)
                null
            }
        } else null
    }

    if (imageBitmap != null) {
        Image(imageBitmap, null, modifierWithSize, contentScale = ContentScale.Inside)
    } else {
        Box(modifierWithSize, contentAlignment = Alignment.Center) {
            if (imageBytes == null)
                CircularProgressIndicator(
                    Modifier.size(48.dp),
                    color = secondaryLabelColor,
                )
            else
                Icon(
                    IconType.ERROR.getImageVector(),
                    null,
                    Modifier.size(48.dp),
                    tint = secondaryLabelColor
                )
        }
    }
}

@Composable
fun MosaikIcon(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as Icon

    val imageVector = element.iconType.getImageVector()

    Icon(
        imageVector, null, modifier.size(
            when (element.iconSize) {
                Icon.Size.SMALL -> 24.dp
                Icon.Size.MEDIUM -> 48.dp
                Icon.Size.LARGE -> 96.dp
            }
        ), tint = foregroundColor(element.tintColor)
    )
}

private fun IconType.getImageVector() =
    when (this) {
        IconType.INFO -> Icons.Default.Info
        IconType.WARN -> Icons.Default.Warning
        IconType.ERROR -> Icons.Default.Error
        IconType.CONFIG -> Icons.Default.Settings
        IconType.ADD -> Icons.Default.Add
        IconType.WALLET -> Icons.Default.AccountBalanceWallet
        IconType.SEND -> Icons.Default.Send
        IconType.RECEIVE -> Icons.Default.CallReceived
        IconType.MORE -> Icons.Default.MoreVert
        IconType.OPENLIST -> Icons.Default.ArrowDropDown
        IconType.CHEVRON_UP -> Icons.Default.ExpandLess
        IconType.CHEVRON_DOWN -> Icons.Default.ExpandMore
        IconType.QR_CODE -> Icons.Default.QrCode
        IconType.QR_SCAN -> Icons.Default.QrCodeScanner
        IconType.COPY -> Icons.Default.ContentCopy
    }

@Composable
fun MosaikLoadingIndicator(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as LoadingIndicator

    CircularProgressIndicator(
        modifier.size(
            when (element.size) {
                LoadingIndicator.Size.SMALL -> 24.dp
                LoadingIndicator.Size.MEDIUM -> 48.dp
            }
        ),
        color = primaryLabelColor,
    )
}

@Composable
fun MosaikTextInputField(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as TextInputField

    // keep everything the user entered, as long as the [ViewTree] is not changed
    val textFieldState =
        remember(treeElement.contentVersion) {
            val currentValue = treeElement.currentValue as String? ?: ""
            mutableStateOf(
                TextFieldValue(
                    currentValue,
                    selection = TextRange(0, currentValue.length)
                )
            )
        }

    Column(modifier.fillMaxWidth()) {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = defaultLabelColor,
            backgroundColor = primaryLabelColor.copy(alpha = 0.4f)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            OutlinedTextField(
                textFieldState.value,
                onValueChange = {
                    if (textFieldState.value.text != it.text) {
                        treeElement.valueChanged(it.text.ifEmpty { null })
                    }
                    textFieldState.value = it
                },
                Modifier.fillMaxWidth(),
                enabled = element.isEnabled,
                isError = !element.errorMessage.isNullOrBlank(),
                maxLines = 1,
                singleLine = true,
                label = { element.placeHolder?.let { Text(it) } },
                trailingIcon = {
                    element.endIcon?.getImageVector()?.let { iv ->
                        val icon = @Composable { Icon(iv, null) }

                        if (element.onEndIconClicked != null)
                            IconButton(onClick = {
                                treeElement.runActionFromUserInteraction(element.onEndIconClicked)
                            }) {
                                icon()
                            }
                        else
                            icon()
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = defaultLabelColor,
                    disabledTextColor = secondaryLabelColor,
                    cursorColor = defaultLabelColor,
                    errorCursorColor = primaryLabelColor,
                    focusedBorderColor = defaultLabelColor,
                    unfocusedBorderColor = secondaryLabelColor,
                    errorBorderColor = primaryLabelColor,
                    disabledLabelColor = secondaryLabelColor,
                    focusedLabelColor = defaultLabelColor,
                    unfocusedLabelColor = secondaryLabelColor,

                    )
            )
        }
        if (!element.errorMessage.isNullOrBlank()) {
            Text(element.errorMessage!!, Modifier.fillMaxWidth(), color = primaryLabelColor)
        }
    }
}

@Composable
private fun MosaikButton(
    treeElement: TreeElement,
    newModifier: Modifier
) {
    val element = treeElement.element as Button

    remember(element.truncationType) {
        if (element.truncationType != TruncationType.END)
            MosaikLogger.logWarning("TruncationType ignored for button, not supported by this implementation")
    }

    if (element.style == Button.ButtonStyle.TEXT) {
        // TODO Use TextButton
        Text(
            element.text ?: "",
            modifier = newModifier.padding(Padding.HALF_DEFAULT.toCompose()),
            maxLines = if (element.maxLines <= 0) Int.MAX_VALUE else element.maxLines,
            textAlign = (when (element.textAlignment) {
                HAlignment.START -> TextAlign.Start
                HAlignment.CENTER -> TextAlign.Center
                HAlignment.END -> TextAlign.End
                HAlignment.JUSTIFY -> TextAlign.Justify
            }),
            color = if (element.isEnabled) textButtonTextColor else textButtonColorDisabled,
            overflow = TextOverflow.Ellipsis,
        )
    } else {
        Button(
            onClick = treeElement::clicked,
            modifier = newModifier,
            colors = when (element.style) {
                Button.ButtonStyle.PRIMARY -> ButtonDefaults.buttonColors(
                    primaryLabelColor,
                    primaryButtonTextColor
                )
                Button.ButtonStyle.SECONDARY -> ButtonDefaults.buttonColors(
                    secondaryButtonColor,
                    secondaryButtonTextColor
                )
                Button.ButtonStyle.TEXT -> throw UnsupportedOperationException("Unreachable code")
            },
            enabled = element.isEnabled
        ) {
            Text(
                element.text ?: "",
                maxLines = if (element.maxLines <= 0) Int.MAX_VALUE else element.maxLines,
                textAlign = (when (element.textAlignment) {
                    HAlignment.START -> TextAlign.Start
                    HAlignment.CENTER -> TextAlign.Center
                    HAlignment.END -> TextAlign.End
                    HAlignment.JUSTIFY -> TextAlign.Justify
                }),
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun MosaikLabel(
    treeElement: TreeElement,
    newModifier: Modifier
) {
    val element = treeElement.element as Label

    if (element.truncationType == TruncationType.MIDDLE && element.maxLines == 1)
        MiddleEllipsisText(
            element.text ?: "",
            newModifier,
            textAlign = (when (element.textAlignment) {
                HAlignment.START -> TextAlign.Start
                HAlignment.CENTER -> TextAlign.Center
                HAlignment.END -> TextAlign.End
                HAlignment.JUSTIFY -> TextAlign.Justify
            }),
            style = labelStyle(element.style),
            color = foregroundColor(element.textColor)
        )
    else {
        // TODO truncationType Start https://stackoverflow.com/a/69084973/7487013
        remember(element.truncationType) {
            if (element.truncationType == TruncationType.START)
                MosaikLogger.logWarning("TruncationType START not supported by this implementation")
            else if (element.truncationType == TruncationType.MIDDLE)
                MosaikLogger.logWarning("TruncationType MIDDLE only supported with maxLines 1 by this implementation")
        }

        Text(
            element.text ?: "",
            newModifier,
            maxLines = if (element.maxLines <= 0) Int.MAX_VALUE else element.maxLines,
            textAlign = (when (element.textAlignment) {
                HAlignment.START -> TextAlign.Start
                HAlignment.CENTER -> TextAlign.Center
                HAlignment.END -> TextAlign.End
                HAlignment.JUSTIFY -> TextAlign.Justify
            }),
            overflow = TextOverflow.Ellipsis,
            style = labelStyle(element.style),
            color = foregroundColor(element.textColor)
        )
    }
}

@Composable
private fun MosaikBox(
    modifier: Modifier,
    treeElement: TreeElement
) {
    val element = treeElement.element as Box

    Box(modifier) {
        treeElement.children.forEach { childElement ->
            key(childElement.idOrHash) {

                val childHAlignment = element.getChildHAlignment(childElement.element)

                MosaikTreeElement(
                    childElement,
                    when (childHAlignment) {
                        HAlignment.JUSTIFY -> Modifier.fillMaxWidth()
                        else -> Modifier
                    }.then(
                        Modifier.align(
                            BiasAlignment(
                                when (childHAlignment) {
                                    HAlignment.START -> -1.0f
                                    HAlignment.CENTER -> 0.0f
                                    HAlignment.END -> 1.0f
                                    HAlignment.JUSTIFY -> 0.0f
                                },
                                when (element.getChildVAlignment(childElement.element)) {
                                    VAlignment.TOP -> -1.0f
                                    VAlignment.CENTER -> 0.0f
                                    VAlignment.BOTTOM -> 1.0f
                                },
                            )
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun MosaikRow(
    modifier: Modifier,
    treeElement: TreeElement,
) {
    val element = treeElement.element as Row

    Row(modifier) {
        treeElement.children.forEach { childElement ->
            key(childElement.idOrHash) {
                val weight = element.getChildWeight(childElement.element)
                MosaikTreeElement(
                    childElement,
                    Modifier.align(
                        when (element.getChildAlignment(childElement.element)) {
                            VAlignment.TOP -> Alignment.Top
                            VAlignment.CENTER -> Alignment.CenterVertically
                            VAlignment.BOTTOM -> Alignment.Bottom
                        }
                    ).then(if (weight > 0) Modifier.weight(weight.toFloat()) else Modifier)

                )
            }
        }
    }
}

@Composable
private fun MosaikColumn(
    modifier: Modifier,
    treeElement: TreeElement,
) {
    val element = treeElement.element as Column

    Column(modifier) {
        treeElement.children.forEach { childElement ->
            key(childElement.idOrHash) {
                val weight = element.getChildWeight(childElement.element)
                val hAlignment = element.getChildAlignment(childElement.element)

                MosaikTreeElement(
                    childElement,
                    when (hAlignment) {
                        HAlignment.START -> Modifier.align(Alignment.Start)
                        HAlignment.CENTER -> Modifier.align(Alignment.CenterHorizontally)
                        HAlignment.END -> Modifier.align(Alignment.End)
                        HAlignment.JUSTIFY -> Modifier.fillMaxWidth()
                    }.then(if (weight > 0) Modifier.weight(weight.toFloat()) else Modifier)
                )
            }
        }
    }
}

private fun Padding.toCompose(): Dp =
    when (this) {
        Padding.NONE -> 0.dp
        Padding.QUARTER_DEFAULT -> 4.dp
        Padding.HALF_DEFAULT -> 8.dp
        Padding.DEFAULT -> 16.dp
        Padding.ONE_AND_A_HALF_DEFAULT -> 24.dp
        Padding.TWICE -> 32.dp
    }

var labelStyle: (LabelStyle) -> TextStyle = { labelStyle ->
    when (labelStyle) {
        LabelStyle.BODY1 -> TextStyle(fontSize = 18.sp)
        LabelStyle.BODY1BOLD -> TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        LabelStyle.BODY1LINK -> TextStyle(
            fontSize = 18.sp,
            textDecoration = TextDecoration.Underline
        )
        LabelStyle.BODY2 -> TextStyle(fontSize = 16.sp)
        LabelStyle.BODY2BOLD -> TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        LabelStyle.HEADLINE1 -> TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        LabelStyle.HEADLINE2 -> TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

var foregroundColor: (ForegroundColor) -> Color = { color ->
    when (color) {
        ForegroundColor.PRIMARY -> primaryLabelColor
        ForegroundColor.DEFAULT -> defaultLabelColor
        ForegroundColor.SECONDARY -> secondaryLabelColor
    }
}