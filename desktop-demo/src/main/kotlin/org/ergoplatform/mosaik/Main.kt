package org.ergoplatform.mosaik

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.ergoplatform.mosaik.model.InitialAppInfo
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
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

        val mosaikContext = MosaikContext(
            1,
            UUID.randomUUID().toString(),
            "EN",
            "demoapp",
            "1",
            MosaikContext.Platform.DESKTOP
        )

        val backendConnector = object : OkHttpBackendConnector(OkHttpClient.Builder()) {
            override fun loadMosaikApp(
                url: String,
                context: MosaikContext
            ): InitialAppInfo {
                val fallback = { MosaikSerializer().firstRequestResponseFromJson(json) }
                return if (url.startsWith("http", true)) {
                    super.loadMosaikApp(url, context)
                } else {
                    fallback()
                }
            }
        }

        val manifestState: MutableState<MosaikManifest?> = mutableStateOf(null)

        val runtime =
            MosaikRuntime(
                coroutineScope = {
                    // for our demo GlobalScope is good to use
                    // for a wallet application, the scope should be bound to the lifecycle of the view
                    // showing the view tree
                    GlobalScope
                },
                backendConnector = backendConnector,
                mosaikContext = mosaikContext,
                showDialog = dialogHandler.showDialog,
                pasteToClipboard = { text ->
                    val selection = StringSelection(text)
                    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                    clipboard.setContents(selection, selection)
                },
                openBrowser = { url ->
                    val osName by lazy(LazyThreadSafetyMode.NONE) {
                        System.getProperty("os.name").lowercase(Locale.getDefault())
                    }
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
                },
                appLoaded = { manifestState.value = it }
            )
        runtime.loadMosaikApp("")
        val viewTree = runtime.viewTree

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
                        textState.value = textState.value.copy(
                            MosaikSerializer()
                                .toJsonBeautified(
                                    ViewContent(
                                        viewTree.actions,
                                        it.second?.element
                                            ?: org.ergoplatform.mosaik.model.ui.layout.Box()
                                    )
                                )
                        )
                        error.value = false
                    }
                    lastChangeFromUser = false
                }
            }

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        MosaikAppHeader(manifestState, runtime)

                        Row {
                            Column(Modifier.weight(2.0f)) {
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
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MosaikAppHeader(
    manifestState: MutableState<MosaikManifest?>,
    runtime: MosaikRuntime
) {
    Column {
        val urlTextFieldState = remember(manifestState.value) {
            mutableStateOf(
                TextFieldValue(runtime.appManifest?.baseUrl ?: "", selection = TextRange(0, 1000))
            )
        }
        Text(manifestState.value?.appName ?: "(No app)")
        TextField(
            urlTextFieldState.value,
            onValueChange = { value -> urlTextFieldState.value = value },
            modifier = Modifier.fillMaxWidth().onKeyEvent {
                if (!runtime.viewTree.uiLocked && it.type == KeyEventType.KeyUp && (it.key == Key.Enter || it.key == Key.NumPadEnter)) {
                    runtime.loadMosaikApp(urlTextFieldState.value.text)
                    true
                } else false
            },
            singleLine = true,
            placeholder = { Text("Enter http url here and hit Return. Blank loads the built-in app") }
        )
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
        MosaikSerializer().viewContentFromJson(json)
    )
    true
} catch (t: Throwable) {
    MosaikLogger.logError("Error deserializing viewtree", t)
    false
}