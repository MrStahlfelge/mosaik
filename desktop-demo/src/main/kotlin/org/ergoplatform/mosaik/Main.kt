package org.ergoplatform.mosaik

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
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

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Mosaik Demo"
        ) {
            val textState = remember { mutableStateOf(TextFieldValue(json)) }
            val viewTree = try {
                ViewTree(
                    UUID.randomUUID().toString()
                ).apply {
                    setRootView(
                        MosaikSerializer().viewElementFromJson(textState.value.text),
                        0L
                    )
                }
            } catch (t: Throwable) {
                println(t.message)
                t.printStackTrace()
                null
            }

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row {
                        if (viewTree != null) {
                            renderTreeElement(viewTree.content!!, Modifier.weight(2.0f))
                        } else {
                            Box(Modifier.weight(2.0f))
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