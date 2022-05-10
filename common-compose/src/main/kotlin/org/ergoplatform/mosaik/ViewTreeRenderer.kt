package org.ergoplatform.mosaik

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.ergoplatform.mosaik.model.ui.layout.Box
import org.ergoplatform.mosaik.model.ui.layout.Column
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label
import java.lang.IllegalArgumentException

@Composable
fun renderTreeElement(treeElement: TreeElement) {
    val element = treeElement.element
    when (element) {
        is Box -> {
            Box {
                treeElement.children.forEach {
                    renderTreeElement(it)
                }
            }
        }
        is Label -> {
            Text(element.text ?: "")
        }
        is Column -> {
            Column {
                treeElement.children.forEach {
                    renderTreeElement(it)
                }
            }
        }
        is Button -> {
            Button(onClick = {
                // TODO
            }) {
                Text(element.text ?: "")
            }
        }
        else -> {
            throw IllegalArgumentException("Unsupported view element: ${element.javaClass.simpleName}")
        }
    }
}