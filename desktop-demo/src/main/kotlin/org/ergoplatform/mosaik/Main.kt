package org.ergoplatform.mosaik

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.ergoplatform.mosaik.serialization.MosaikSerializer
import java.util.*

fun main() {

    application {
        val windowState = rememberWindowState()

        val json = this.javaClass.getResource("/default_tree.json")!!.readText()
        val renderer = ViewTreeRenderer()
        val viewTree = ViewTree(
            UUID.randomUUID().toString(),
            ActionRunner()
        )

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Mosaik Demo"
        ) {
            val textState = remember { mutableStateOf(TextFieldValue(json)) }
            val success = try {
                viewTree.setRootView(
                    MosaikSerializer().viewElementFromJson(textState.value.text),
                    0L
                )
                true
            } catch (t: Throwable) {
                println(t.message)
                t.printStackTrace()
                false
            }
            val content by viewTree.contentState.collectAsState()

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        Column(Modifier.weight(2.0f)) {
                            if (success) {
                                renderer.renderTreeElement(content!!, Modifier.fillMaxWidth().weight(1.0f, false))
                            }
                        }
                        TextField(textState.value, onValueChange = {
                            textState.value = it
                        }, Modifier.weight(1.0f))
                    }
                }
            }
        }
    }
}