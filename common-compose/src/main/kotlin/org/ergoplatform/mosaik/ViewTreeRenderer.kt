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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label

@Composable
fun renderTreeElement(treeElement: TreeElement, modifier: Modifier = Modifier) {
    val element = treeElement.element

    val newModifier = if (element is LayoutElement) {
        modifier.padding(element.padding.toCompose())
    } else {
        modifier
    }

    when (element) {
        is Box -> {
            renderBox(newModifier, treeElement)
        }
        is Label -> {
            Text(element.text ?: "", newModifier)
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