package org.ergoplatform.mosaik

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.ergoplatform.mosaik.model.ui.text.TruncationType

open class ViewTreeRenderer {
    @Composable
    fun renderTreeElement(treeElement: TreeElement, modifier: Modifier = Modifier) {
        val element = treeElement.element

        val newModifier = if (element is LayoutElement) {
            modifier.padding(element.padding.toCompose())
        } else {
            modifier
        }.alpha(if (element.isVisible) 1.0f else 0.0f)
        // TODO: long press, on click

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
                Button(
                    onClick = {
                        // TODO
                    }, modifier = newModifier
                ) {
                    Text(element.text ?: "")
                }
            }
            else -> {
                throw IllegalArgumentException("Unsupported view element: ${element.javaClass.simpleName}")
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
                style = element.style.toCompose(),
                color = element.textColor.toCompose()
            )
        else // TODO truncationType Start https://stackoverflow.com/a/69084973/7487013
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
                style = element.style.toCompose(),
                color = element.textColor.toCompose()
            )
    }

    @Composable
    private fun renderBox(
        modifier: Modifier,
        treeElement: TreeElement
    ) {
        val element = treeElement.element as Box

        Box(modifier) {
            treeElement.children.forEach {
                renderTreeElement(
                    it,
                    Modifier.align(
                        BiasAlignment(
                            when (element.getChildHAlignment(it.element)) {
                                HAlignment.START -> -1.0f
                                HAlignment.CENTER -> 0.0f
                                HAlignment.END -> 1.0f
                            },
                            when (element.getChildVAlignment(it.element)) {
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

    @Composable
    private fun renderRow(
        modifier: Modifier,
        treeElement: TreeElement,
    ) {
        val element = treeElement.element as Row

        Row(modifier) {
            treeElement.children.forEach {
                renderTreeElement(
                    it,
                    Modifier.align(element.getChildAlignment(it.element).toCompose())
                )
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
            treeElement.children.forEach {
                renderTreeElement(
                    it,
                    Modifier.align(element.getChildAlignment(it.element).toCompose())
                )
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

    open fun LabelStyle.toCompose(): TextStyle =
        when (this) {
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

    open fun ForegroundColor.toCompose(): Color =
        when (this) {
            ForegroundColor.PRIMARY -> Color(0xffff3b30)
            ForegroundColor.DEFAULT -> Color.Unspecified
            ForegroundColor.SECONDARY -> Color(0xffBBBBBB)
        }
}