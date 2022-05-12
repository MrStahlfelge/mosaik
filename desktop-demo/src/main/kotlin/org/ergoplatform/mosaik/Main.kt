package org.ergoplatform.mosaik

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.ergoplatform.mosaik.serialization.MosaikSerializer
import java.util.*

fun main() {

    application {
        val windowState = rememberWindowState()

        MosaikLogger.logger = MosaikLogger.DefaultLogger

        val json = this.javaClass.getResource("/default_tree.json")!!.readText()
        val viewTree = ViewTree(
            UUID.randomUUID().toString(),
            ActionRunner()
        )
        updateViewTreeFromJson(viewTree, json)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Mosaik Demo"
        ) {
            val textState = remember { mutableStateOf(TextFieldValue(json)) }
            val error = remember { mutableStateOf(false) }

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        Column(Modifier.weight(2.0f).padding(20.dp)) {
                            MosaikViewTree(viewTree, Modifier.fillMaxWidth())
                        }
                        TextField(
                            textState.value,
                            onValueChange = {
                                val textChanged = textState.value.text != it.text
                                val json = it.text
                                textState.value = it
                                if (textChanged) {
                                    error.value = !updateViewTreeFromJson(viewTree, json)
                                }

                            },
                            Modifier.weight(1.0f)
                                .background(if (error.value) Color.Red else Color.Unspecified)
                        )
                    }
                }
            }
        }
    }
}

private fun updateViewTreeFromJson(
    viewTree: ViewTree,
    json: String
) = try {
    viewTree.setRootView(
        MosaikSerializer().viewElementFromJson(json),
        0L
    )
    true
} catch (t: Throwable) {
    MosaikLogger.logError("Error deserializing viewtree", t)
    false
}