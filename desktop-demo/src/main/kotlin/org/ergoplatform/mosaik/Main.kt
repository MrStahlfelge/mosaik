package org.ergoplatform.mosaik

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.ergoplatform.mosaik.serialization.MosaikSerializer
import java.util.*

fun main() {

    application {
        val windowState = rememberWindowState()

        val viewTree = ViewTree(
            UUID.randomUUID().toString()
        ).apply {
            setRootView(
                MosaikSerializer().viewElementFromJson(
                    this.javaClass.getResource("/default_tree.json")!!.readText()
                ),
                0L
            )
        }

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Mosaik Demo"
        ) {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    renderTreeElement(viewTree.content!!)
                }
            }
        }
    }
}