package org.ergoplatform.mosaik

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.input.TextInputField
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.ergoplatform.mosaik.model.ui.text.TruncationType

@Composable
fun MosaikViewTree(viewTree: ViewTree, modifier: Modifier = Modifier) {
    val modification by viewTree.contentState.collectAsState()
    modification.second?.let { viewTreeTarget ->
        Crossfade(targetState = viewTreeTarget) { viewTreeRoot ->
            MosaikTreeElement(viewTreeRoot, modifier)
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
        .then(if (treeElement.respondsToClick)
            Modifier.combinedClickable(
                onClick = treeElement::clicked,
                onLongClick = {
                    // TODO
                }
            ) else Modifier)

    when (element) {
        is Box -> {
            renderBox(newModifier, treeElement)
        }
        is Label -> {
            renderLabel(treeElement, newModifier)
        }
        is Column -> {
            renderColumn(newModifier, treeElement)
        }
        is Row -> {
            renderRow(newModifier, treeElement)
        }
        is Button -> {
            renderButton(treeElement, newModifier)
        }
        is TextInputField -> {
            renderTextInputField(treeElement, newModifier)
        }
        else -> {
            throw IllegalArgumentException("Unsupported view element: ${element.javaClass.simpleName}")
        }
    }
}

@Composable
fun renderTextInputField(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as TextInputField

    // TODO IconType endIconType, Action onEndIconClicked;

    // keep everything the user entered, as long as the [ViewTree] is not changed
    val textFieldState =
        remember(treeElement.contentVersion) {
            mutableStateOf(
                TextFieldValue(
                    treeElement.currentValue as String? ?: ""
                )
            )
        }

    Column(modifier.fillMaxWidth()) {
        OutlinedTextField(
            textFieldState.value,
            onValueChange = {
                textFieldState.value = it
                treeElement.valueChanged(it.text.ifEmpty { null })

                // TODO onValueChangedAction
                //  should not be called on every change, but only value change and after some time
            },
            Modifier.fillMaxWidth(),
            enabled = element.isEnabled,
            isError = !element.errorMessage.isNullOrBlank(),
            maxLines = 1,
            label = { element.placeHolder?.let { Text(it) } },
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
        if (!element.errorMessage.isNullOrBlank()) {
            Text(element.errorMessage!!, Modifier.fillMaxWidth(), color = primaryLabelColor)
        }
    }
}

@Composable
private fun renderButton(
    treeElement: TreeElement,
    newModifier: Modifier
) {
    val element = treeElement.element as Button
    if (element.style == Button.ButtonStyle.TEXT) {
        Text(
            element.text ?: "",
            modifier = newModifier.padding(Padding.HALF_DEFAULT.toCompose()),
            maxLines = if (element.maxLines <= 0) Int.MAX_VALUE else element.maxLines,
            textAlign = (when (element.textAlignment) {
                HAlignment.START -> TextAlign.Start
                HAlignment.CENTER -> TextAlign.Center
                HAlignment.END -> TextAlign.End
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
                }),
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun renderLabel(
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
            }),
            overflow = TextOverflow.Ellipsis,
            style = labelStyle(element.style),
            color = foregroundColor(element.textColor)
        )
    }
}

@Composable
private fun renderBox(
    modifier: Modifier,
    treeElement: TreeElement
) {
    val element = treeElement.element as Box

    Box(modifier) {
        treeElement.children.forEach { childElement ->
            key(childElement.id ?: childElement.hashCode().toString()) {
                MosaikTreeElement(
                    childElement,
                    Modifier.align(
                        BiasAlignment(
                            when (element.getChildHAlignment(childElement.element)) {
                                HAlignment.START -> -1.0f
                                HAlignment.CENTER -> 0.0f
                                HAlignment.END -> 1.0f
                            },
                            when (element.getChildVAlignment(childElement.element)) {
                                VAlignment.TOP -> -1.0f
                                VAlignment.CENTER -> 0.0f
                                VAlignment.BOTTOM -> 1.0f
                            },
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun renderRow(
    modifier: Modifier,
    treeElement: TreeElement,
) {
    val element = treeElement.element as Row

    Row(modifier) {
        treeElement.children.forEach { childElement ->
            key(childElement.id ?: childElement.hashCode()) {
                val weight = element.getChildWeight(childElement.element)
                MosaikTreeElement(
                    childElement,
                    Modifier.align(element.getChildAlignment(childElement.element).toCompose())
                        .then(if (weight > 0) Modifier.weight(weight.toFloat()) else Modifier)

                )
            }
        }
    }
}

@Composable
private fun renderColumn(
    modifier: Modifier,
    treeElement: TreeElement,
) {
    val element = treeElement.element as Column

    Column(modifier) {
        treeElement.children.forEach { childElement ->
            key(childElement.id ?: childElement.hashCode().toString()) {
                val weight = element.getChildWeight(childElement.element)
                MosaikTreeElement(
                    childElement,
                    Modifier.align(element.getChildAlignment(childElement.element).toCompose())
                        .then(if (weight > 0) Modifier.weight(weight.toFloat()) else Modifier)
                )
            }
        }
    }
}

private fun HAlignment.toCompose(): Alignment.Horizontal =
    when (this) {
        HAlignment.START -> Alignment.Start
        HAlignment.CENTER -> Alignment.CenterHorizontally
        HAlignment.END -> Alignment.End
    }

private fun VAlignment.toCompose(): Alignment.Vertical =
    when (this) {
        VAlignment.TOP -> Alignment.Top
        VAlignment.CENTER -> Alignment.CenterVertically
        VAlignment.BOTTOM -> Alignment.Bottom
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