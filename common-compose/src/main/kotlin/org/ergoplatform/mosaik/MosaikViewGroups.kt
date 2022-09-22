package org.ergoplatform.mosaik

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ergoplatform.mosaik.model.ui.layout.Grid

@Composable
internal fun MosaikGrid(
    treeElement: TreeElement,
    modifier: Modifier
) {
    val element = treeElement.element as Grid

    val minColumWidth = when (element.elementSize) {
        Grid.ElementSize.MIN -> 180.dp
        Grid.ElementSize.SMALL -> 300.dp
        Grid.ElementSize.MEDIUM -> 420.dp
        Grid.ElementSize.LARGE -> 520.dp
    }

    BoxWithConstraints(modifier) {
        val numRows = (maxWidth / minColumWidth).toInt()

        Column {

            if (numRows <= 1) {
                // only a simple column
                treeElement.children.forEach { childElement ->
                    key(childElement.idOrUuid) {

                        MosaikTreeElement(
                            childElement,
                            Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                treeElement.children.chunked(numRows).forEach { rowElements ->
                    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        rowElements.forEach { childElement ->
                            key(childElement.idOrUuid) {
                                MosaikTreeElement(childElement, Modifier.weight(1f).fillMaxHeight())
                            }
                        }
                        for (i in 1..(numRows - rowElements.size)) {
                            Box(Modifier.weight(1f))
                        }
                    }

                }
            }
        }
    }
}

