package org.ergoplatform.mosaik

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.input.KeyboardType
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
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ui.*
import org.ergoplatform.mosaik.model.ui.input.*
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.*

@Composable
fun MosaikViewTree(viewTree: ViewTree, modifier: Modifier = Modifier) {
    val modification by viewTree.contentState.collectAsState()
    val locked by viewTree.uiLockedState.collectAsState()

    Box(modifier) {
        modification.second?.let { viewTreeRoot ->
            // Crossfade animation here caused some elements to not update

            // the view root should be scrollable if it is a column, otherwise it will fill
            // the max height
            val scrollable = viewTreeRoot.element is Column
            val scrollState =
                rememberSaveable(viewTreeRoot.createdAtContentVersion, saver = ScrollState.Saver) {
                    ScrollState(0)
                }
            val sizeModifier =
                if (scrollable && MosaikComposeConfig.VerticalScrollbar != null) {
                    Modifier.fillMaxWidth().verticalScroll(scrollState)
                } else if (scrollable) {
                    Modifier.fillMaxWidth()
                        .drawVerticalScrollbar(scrollState)
                        .verticalScroll(scrollState)
                } else Modifier.fillMaxSize()

            Box(sizeModifier) {
                MosaikTreeElement(
                    viewTreeRoot,
                    Modifier.alpha(if (locked) .3f else 1f)
                        .align(Alignment.Center).widthIn(
                            max = when (viewTree.targetCanvasDimension) {
                                // https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
                                MosaikManifest.CanvasDimension.COMPACT_WIDTH -> 600.dp
                                MosaikManifest.CanvasDimension.MEDIUM_WIDTH -> 840.dp
                                else -> Dp.Unspecified
                            }
                        )
                )
            }

            MosaikComposeConfig.VerticalScrollbar?.invoke(this, scrollState)
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
        is Card -> {
            MosaikCard(newModifier, treeElement)
        }
        is Box -> {
            // this also deals with LazyLoadBox
            MosaikBox(newModifier, treeElement)
        }

        is CheckboxLabel -> MosaikCheckboxLabel(treeElement, newModifier)

        is StyleableTextLabel<*> -> {
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
        is ErgAmountInputField -> {
            MosaikErgAmountInputLayout(treeElement, newModifier)
        }
        is TextField<*> -> {
            MosaikTextField(treeElement, newModifier)
        }
        is DropDownList -> {
            MosaikDropDownList(treeElement, newModifier)
        }
        is LoadingIndicator -> {
            MosaikLoadingIndicator(treeElement, newModifier)
        }
        is Icon -> {
            MosaikIcon(treeElement, newModifier)
        }
        is Image -> {
            MosaikImage(treeElement, newModifier)
        }

        is QrCode -> MosaikQrCode(treeElement, newModifier)

        is StyleableInputButton<*> -> {
            MosaikInputButton(treeElement, newModifier)
        }
        is HorizontalRule -> {
            MosaikHorizontalRule(treeElement, newModifier)
        }

        is MarkDown -> MosaikMarkDown(treeElement, newModifier)

        else -> {
            Text("Unsupported view element: ${element.javaClass.simpleName}", newModifier)
        }
    }
}

@Composable
fun MosaikHorizontalRule(treeElement: TreeElement, newModifier: Modifier) {
    val element = treeElement.element as HorizontalRule

    Divider(
        newModifier.padding(vertical = element.getvPadding().toCompose()),
        color = secondaryLabelColor
    )
}

@Composable
fun MosaikInputButton(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as StyleableInputButton<*>

    val valueState = treeElement.viewTree.valueState.collectAsState()

    when (element.style) {
        StyleableInputButton.InputButtonStyle.BUTTON_PRIMARY,
        StyleableInputButton.InputButtonStyle.BUTTON_SECONDARY -> {
            // remember this to not fire up any logic (db access etc) to retrieve the label
            val buttonLabel =
                remember(valueState.value.second[treeElement.element.id]?.inputValue) { treeElement.currentValueAsString }

            Button(
                onClick = treeElement::clicked,
                modifier = modifier.widthIn(min = 96.dp * 2, max = 96.dp * 3),
                colors = when (element.style) {
                    StyleableInputButton.InputButtonStyle.BUTTON_PRIMARY -> Button.ButtonStyle.PRIMARY
                    StyleableInputButton.InputButtonStyle.BUTTON_SECONDARY -> Button.ButtonStyle.SECONDARY
                    else -> throw IllegalStateException("Unreachable")
                }.toButtonColors(),
                enabled = element.isEnabled
            ) {
                Text(
                    buttonLabel,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        StyleableInputButton.InputButtonStyle.ICON_PRIMARY,
        StyleableInputButton.InputButtonStyle.ICON_SECONDARY -> Icon(
            IconType.WALLET.getImageVector(),
            null,
            modifier.size(48.dp),
            tint = foregroundColor(
                if (!element.isEnabled) ForegroundColor.SECONDARY
                else if (element.style == StyleableInputButton.InputButtonStyle.ICON_PRIMARY) ForegroundColor.PRIMARY
                else ForegroundColor.DEFAULT
            )
        )
    }
}

@Composable
fun MosaikQrCode(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as QrCode

    MosaikComposeConfig.convertQrCodeContentToImageBitmap?.let { convertMethod ->
        val qrCodeImage = remember(treeElement.createdAtContentVersion) {
            convertMethod(element.content)
        }

        qrCodeImage?.let {
            Image(
                qrCodeImage,
                null,
                modifier.width(MosaikComposeConfig.qrCodeSize)
            )
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
                MosaikComposeConfig.convertByteArrayToImageBitmap(imageBytes)
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
        IconType.EDIT -> Icons.Default.Edit
        IconType.BACK -> Icons.Default.ArrowBack
        IconType.FORWARD -> Icons.Default.ArrowForward
        IconType.SWITCH -> Icons.Default.SyncAlt
        IconType.REFRESH -> Icons.Default.Refresh
        IconType.DELETE -> Icons.Default.Delete
        IconType.CROSS -> Icons.Default.Close
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
fun MosaikErgAmountInputLayout(treeElement: TreeElement, modifier: Modifier) {
    val mosaikRuntime = treeElement.viewTree.mosaikRuntime
    val fiatAmountAvailable = mosaikRuntime.fiatRate != null

    if (fiatAmountAvailable) {
        val fiatOrErgTextInputHandler =
            treeElement.inputValueHandler as FiatOrErgTextInputHandler

        var secondLabelState by remember {
            mutableStateOf(
                Pair(
                    treeElement.currentValue as Long?,
                    fiatOrErgTextInputHandler.inputIsFiat
                )
            )
        }
        val textFieldState = getTextFieldStateForElement(treeElement)

        Column(modifier) {
            MosaikTextField(treeElement, Modifier, textFieldState) { changedValue ->
                secondLabelState =
                    Pair(changedValue as Long?, fiatOrErgTextInputHandler.inputIsFiat)
            }

            val canChangeInputMode = fiatOrErgTextInputHandler.canChangeInputMode()
            Row((if (canChangeInputMode) Modifier.clickable {
                fiatOrErgTextInputHandler.switchInputAmountMode()
                textFieldState.value = TextFieldValue(treeElement.currentValueAsString)
                secondLabelState =
                    Pair(treeElement.currentValue as Long?, fiatOrErgTextInputHandler.inputIsFiat)
            } else Modifier).align(Alignment.End).padding(horizontal = 4.dp)) {

                // when the input mode is switchable, we always show 0.00 amount for better
                // clickability and to show which input type is active.
                // if it is not switchable, nothing is shown for blank amounts
                val nanoErgsToShow =
                    if (fiatOrErgTextInputHandler.canChangeInputMode()) secondLabelState.first
                        ?: 0 else secondLabelState.first

                Text(
                    nanoErgsToShow?.let { fiatOrErgTextInputHandler.getSecondLabelString(it) }
                        ?: "",
                    textAlign = TextAlign.End,
                    style = labelStyle(LabelStyle.BODY1),
                    color = foregroundColor(ForegroundColor.SECONDARY)
                )

                if (canChangeInputMode) {
                    Icon(
                        IconType.SWITCH.getImageVector(),
                        null,
                        Modifier.padding(start = 4.dp),
                        tint = foregroundColor(ForegroundColor.SECONDARY)
                    )
                }
            }
        }
    } else {
        MosaikTextField(treeElement, modifier)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MosaikTextField(
    treeElement: TreeElement,
    modifier: Modifier,
    textFieldState: MutableState<TextFieldValue> = getTextFieldStateForElement(treeElement),
    valueChangeCallback: ((Any?) -> Unit)? = null
) {
    val element = treeElement.element as TextField<*>

    val errorState = remember(treeElement.createdAtContentVersion) { mutableStateOf(false) }

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
                        errorState.value =
                            !treeElement.changeValueFromInput(it.text.ifEmpty { null })
                        valueChangeCallback?.invoke(treeElement.currentValue)
                    }
                    textFieldState.value = it
                },
                Modifier.fillMaxWidth()
                    .then(if (MosaikComposeConfig.interceptReturnForImeAction && element.onImeAction != null)
                        Modifier.onKeyEvent {
                            if (it.type == KeyEventType.KeyUp && (it.key == Key.Enter || it.key == Key.NumPadEnter)) {
                                treeElement.runActionFromUserInteraction(element.onImeAction)
                                true
                            } else false
                        }
                    else Modifier),
                enabled = element.isEnabled,
                isError = errorState.value,
                maxLines = 1,
                singleLine = true,
                visualTransformation = if (element is PasswordInputField)
                    PasswordVisualTransformation() else VisualTransformation.None,
                keyboardActions = KeyboardActions(
                    onDone = element.onImeAction?.let { action ->
                        {
                            if (element.imeActionType == TextField.ImeActionType.DONE)
                                treeElement.runActionFromUserInteraction(action)
                        }
                    },
                    onGo = element.onImeAction?.let { action ->
                        {
                            if (element.imeActionType == TextField.ImeActionType.GO)
                                treeElement.runActionFromUserInteraction(action)
                        }
                    },
                    onSearch = element.onImeAction?.let { action ->
                        {
                            if (element.imeActionType == TextField.ImeActionType.SEARCH)
                                treeElement.runActionFromUserInteraction(action)
                        }
                    }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = when (treeElement.keyboardType) {
                        org.ergoplatform.mosaik.KeyboardType.Text -> KeyboardType.Text
                        org.ergoplatform.mosaik.KeyboardType.Number -> KeyboardType.Number
                        org.ergoplatform.mosaik.KeyboardType.NumberDecimal -> KeyboardType.Number
                        org.ergoplatform.mosaik.KeyboardType.Email -> KeyboardType.Email
                        org.ergoplatform.mosaik.KeyboardType.Password -> KeyboardType.Password
                    },
                    imeAction = when (element.imeActionType) {
                        TextField.ImeActionType.NEXT -> ImeAction.Next
                        TextField.ImeActionType.DONE -> ImeAction.Done
                        TextField.ImeActionType.SEARCH -> ImeAction.Search
                        TextField.ImeActionType.GO -> ImeAction.Go
                    }
                ),
                readOnly = element.isReadOnly,
                label = { element.placeholder?.let { Text(it) } },
                trailingIcon = element.endIcon?.getImageVector()?.let { iv ->
                    {
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
                    trailingIconColor = secondaryLabelColor,
                )
            )
        }
        if (!element.errorMessage.isNullOrBlank()) {
            Text(element.errorMessage!!, Modifier.fillMaxWidth(), color = primaryLabelColor)
        }
    }
}

@Composable
private fun getTextFieldStateForElement(treeElement: TreeElement) =
    // keep everything the user entered, as long as the [ViewTree] is not changed
    remember(treeElement.createdAtContentVersion) {
        val currentValue = treeElement.currentValueAsString
        val selectAll =
            (treeElement.element as? TextField<*>)?.let { !it.isReadOnly && it.isEnabled } ?: false
        mutableStateOf(
            TextFieldValue(
                currentValue,
                selection = TextRange(0, if (selectAll) currentValue.length else 0)
            )
        )
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

    val buttonModifier = newModifier.widthIn(128.dp)

    if (element.style == Button.ButtonStyle.TEXT) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = buttonModifier.padding(Padding.HALF_DEFAULT.toCompose()),
        ) {
            Text(
                element.text ?: "",
                maxLines = if (element.maxLines <= 0) Int.MAX_VALUE else element.maxLines,
                textAlign = element.textAlignment.toTextAlign(),
                color = if (element.isEnabled) textButtonTextColor else textButtonColorDisabled,
                overflow = TextOverflow.Ellipsis,
                style = labelStyle(LabelStyle.BODY2BOLD),
            )
        }
    } else {
        Button(
            onClick = treeElement::clicked,
            modifier = buttonModifier,
            colors = element.style.toButtonColors(),
            enabled = element.isEnabled
        ) {
            Text(
                element.text ?: "",
                maxLines = if (element.maxLines <= 0) Int.MAX_VALUE else element.maxLines,
                textAlign = element.textAlignment.toTextAlign(),
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun Button.ButtonStyle.toButtonColors() =
    when (this) {
        Button.ButtonStyle.PRIMARY -> ButtonDefaults.buttonColors(
            primaryLabelColor,
            primaryButtonTextColor
        )
        Button.ButtonStyle.SECONDARY -> ButtonDefaults.buttonColors(
            secondaryButtonColor,
            secondaryButtonTextColor
        )
        Button.ButtonStyle.TEXT -> throw UnsupportedOperationException("Unreachable code")
    }

@Composable
private fun MosaikCheckboxLabel(
    treeElement: TreeElement,
    newModifier: Modifier
) {
    val element = treeElement.element as CheckboxLabel
    val state = remember(treeElement.createdAtContentVersion) {
        mutableStateOf(treeElement.currentValue as Boolean?)
    }
    val clickModifier = if (element.isEnabled) {
        Modifier.clickable {
            state.value = !(state.value ?: false)
            treeElement.valueChanged(state.value)
        }
    } else Modifier

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = newModifier
            .then(clickModifier)
            .padding(4.dp)
    ) {
        Checkbox(
            checked = state.value ?: false,
            onCheckedChange = null,
            enabled = element.isEnabled,
            colors = CheckboxDefaults.colors(checkedColor = primaryLabelColor)
        )

        Spacer(Modifier.size(8.dp))

        MosaikLabel(treeElement, Modifier)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MosaikLabel(
    treeElement: TreeElement,
    modifier: Modifier
) {
    val element = treeElement.element as StyleableTextLabel<*>
    val text = remember(treeElement.createdAtContentVersion) {
        LabelFormatter.getFormattedText(element, treeElement)
    }

    val expandable = (element is ExpandableElement && element.isExpandOnClick)
    val expanded = remember { mutableStateOf(false) }

    val newModifier = if (expandable) {
        // we already have onLongClick defined by MosaikTreeElement(), but it is overwritten
        // by clickable() - so we need to set the combinedClickable again
        modifier.combinedClickable(
            onClick = { expanded.value = !expanded.value },
            onLongClick = treeElement::longPressed,
        )
    } else modifier

    val maxLines = if (expandable && !expanded.value) 1 else element.maxLines

    if (text != null) {
        if (element.truncationType == TruncationType.MIDDLE && maxLines == 1)
            MiddleEllipsisText(
                text,
                newModifier,
                textAlign = element.textAlignment.toTextAlign(),
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
                text,
                newModifier,
                maxLines = if (maxLines <= 0) Int.MAX_VALUE else maxLines,
                textAlign = element.textAlignment.toTextAlign(),
                overflow = TextOverflow.Ellipsis,
                style = labelStyle(element.style),
                color = foregroundColor(element.textColor)
            )
        }
    }
}

@Composable
fun MosaikCard(modifier: Modifier, treeElement: TreeElement) {
    Card(modifier, elevation = 4.dp) {
        MosaikBox(Modifier, treeElement)
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
            key(childElement.idOrUuid) {

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

    val rowModifier = if (element.isPacked) modifier.width(IntrinsicSize.Min) else modifier

    Row(rowModifier) {
        treeElement.children.forEachIndexed { index, childElement ->
            key(childElement.idOrUuid) {
                val weight = element.getChildWeight(childElement.element)
                MosaikTreeElement(
                    childElement,
                    Modifier.align(
                        when (element.getChildAlignment(childElement.element)) {
                            VAlignment.TOP -> Alignment.Top
                            VAlignment.CENTER -> Alignment.CenterVertically
                            VAlignment.BOTTOM -> Alignment.Bottom
                        }
                    ).then(if (weight > 0) Modifier.weight(weight.toFloat()) else Modifier).then(
                        if (index > 0 && element.spacing != Padding.NONE) Modifier.padding(start = element.spacing.toCompose())
                        else Modifier
                    )

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
    val childrenWithWeightAndAlignment: List<Triple<TreeElement, Int, HAlignment>> =
        treeElement.children.map { childElement ->
            Triple(
                childElement,
                element.getChildWeight(childElement.element),
                element.getChildAlignment(childElement.element)
            )
        }
    val weightSum = childrenWithWeightAndAlignment.sumOf { it.second }

    // height(IntrinsicSize.Min) can cause clipping on last element, but it is mandatory if
    // weights are used. To get both working as good as possible, set it only when needed
    val columnModifier = if (weightSum > 0) modifier.height(IntrinsicSize.Min) else modifier

    Column(columnModifier) {
        childrenWithWeightAndAlignment.forEachIndexed { index, (childElement, weight, hAlignment) ->
            key(childElement.idOrUuid) {
                MosaikTreeElement(
                    childElement,
                    when (hAlignment) {
                        HAlignment.START -> Modifier.align(Alignment.Start)
                        HAlignment.CENTER -> Modifier.align(Alignment.CenterHorizontally)
                        HAlignment.END -> Modifier.align(Alignment.End)
                        HAlignment.JUSTIFY -> Modifier.fillMaxWidth()
                    }.then(if (weight > 0) Modifier.weight(weight.toFloat()) else Modifier).then(
                        if (index > 0 && element.spacing != Padding.NONE) Modifier.padding(top = element.spacing.toCompose())
                        else Modifier
                    )
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

fun HAlignment.toTextAlign(): TextAlign =
    when (this) {
        HAlignment.START -> TextAlign.Start
        HAlignment.CENTER -> TextAlign.Center
        HAlignment.END -> TextAlign.End
        HAlignment.JUSTIFY -> TextAlign.Justify
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
