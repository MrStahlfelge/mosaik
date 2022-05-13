package org.ergoplatform.mosaik

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.ergoplatform.mosaik.serialization.MosaikSerializer
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI
import java.util.*


fun main() {

    application {
        val windowState = rememberWindowState()

        MosaikLogger.logger = MosaikLogger.DefaultLogger

        val json = this.javaClass.getResource("/default_tree.json")!!.readText()

        val dialogHandler = MosaikComposeDialogHandler()
        val viewTree = ViewTree(
            UUID.randomUUID().toString(),
            ActionRunner(
                coroutineScope = {
                    // for our demo GlobalScope is good to use
                    // for a wallet application, the scope should be bound to the lifecycle of the view
                    // showing the view tree
                    GlobalScope
                },
                showDialog = dialogHandler.showDialog,
                pasteToClipboard = { text ->
                    val selection = StringSelection(text)
                    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                    clipboard.setContents(selection, selection)
                },
                openBrowser = { url ->
                    val osName by lazy(LazyThreadSafetyMode.NONE) { System.getProperty("os.name").lowercase(Locale.getDefault()) }
                    val desktop = Desktop.getDesktop()
                    when {
                        Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE) -> {
                            desktop.browse(URI(url))
                            true
                        }
                        "mac" in osName -> {
                            Runtime.getRuntime().exec("open $url")
                            true
                        }
                        "nix" in osName || "nux" in osName -> {
                            Runtime.getRuntime().exec("xdg-open $url")
                            true
                        }
                        else -> false
                    }
                }
            )
        )
        updateViewTreeFromJson(viewTree, json)

        var lastChangeFromUser = false

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Mosaik Demo"
        ) {
            val textState = remember { mutableStateOf(TextFieldValue(json)) }
            val error = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            scope.launch {
                viewTree.contentState.collect {
                    if (!lastChangeFromUser) {
                        textState.value =
                            textState.value.copy(MosaikSerializer().toJsonBeautified(it.second!!.element))
                        error.value = false
                    }
                    lastChangeFromUser = false
                }
            }

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        Column(Modifier.weight(2.0f).padding(20.dp)) {
                            MosaikViewTree(viewTree, Modifier.fillMaxWidth())
                            MosaikComposeDialog(dialogHandler)
                        }

                        Column(Modifier.weight(1.0f).fillMaxSize()) {
                            MosaikStateInfo(
                                viewTree,
                                textState,
                                error,
                                setLastChangeFromUser = { lastChangeFromUser = true }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MosaikStateInfo(
    viewTree: ViewTree,
    textState: MutableState<TextFieldValue>,
    error: MutableState<Boolean>,
    setLastChangeFromUser: (() -> Unit)
) {
    Text("Current values:", fontWeight = FontWeight.Bold)
    val map = viewTree.valueState.collectAsState()
    map.value.second.forEach { (key, value) ->
        Text(key + ": " + value.toString())
    }
    Box(Modifier.height(20.dp))
    Text("Current view tree (${viewTree.contentState.value.first}):", fontWeight = FontWeight.Bold)
    TextField(
        textState.value,
        onValueChange = {
            val textChanged = textState.value.text != it.text
            val newJson = it.text
            textState.value = it
            if (textChanged) {
                setLastChangeFromUser()
                error.value = !updateViewTreeFromJson(viewTree, newJson)
            }

        },
        Modifier
            .background(if (error.value) Color.Red else Color.Unspecified)
            .fillMaxHeight()
    )
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