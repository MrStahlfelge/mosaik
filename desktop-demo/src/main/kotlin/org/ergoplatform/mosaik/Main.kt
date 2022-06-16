package org.ergoplatform.mosaik

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.ErgoAuthAction
import org.ergoplatform.mosaik.model.actions.ErgoPayAction
import org.ergoplatform.mosaik.model.actions.TokenInformationAction
import org.ergoplatform.mosaik.serialization.MosaikSerializer
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URI
import java.util.*


fun main() {

    application {
        val windowState = rememberWindowState()

        MosaikLogger.logger = MosaikLogger.DefaultLogger
        MosaikComposeConfig.convertByteArrayToImageBitmap =
            { imageBytes -> loadImageBitmap(imageBytes.inputStream()) }
        MosaikComposeConfig.interceptReturnForImeAction = true

        val json = this.javaClass.getResource("/default_tree.json")!!.readText()

        val dialogHandler = MosaikComposeDialogHandler()

        val mosaikContext = MosaikContext(
            MosaikContext.LIBRARY_MOSAIK_VERSION,
            UUID.randomUUID().toString(),
            Locale.getDefault().language,
            "demoapp",
            "1",
            MosaikContext.Platform.DESKTOP
        )

        val backendConnector =
            object : OkHttpBackendConnector(OkHttpClient.Builder(), { mosaikContext }) {
                override fun loadMosaikApp(
                    url: String,
                    referrer: String?,
                ): MosaikBackendConnector.AppLoaded {
                    return if (url.isBlank()) {
                        MosaikBackendConnector.AppLoaded(
                            MosaikSerializer().firstRequestResponseFromJson(
                                json
                            ), url
                        )
                    } else {
                        val urlToUse = if (url.contains("://")) url else "http://$url"
                        super.loadMosaikApp(urlToUse, referrer)
                    }
                }
            }

        val manifestState: MutableState<MosaikManifest?> = mutableStateOf(null)

        val runtime =
            object : MosaikRuntime(
                backendConnector = backendConnector,
            ) {
                override val coroutineScope: CoroutineScope
                    // for our demo GlobalScope is good to use
                    // for a wallet application, the scope should be bound to the lifecycle of the view
                    // showing the view tree
                    get() = GlobalScope

                override fun showDialog(dialog: MosaikDialog) {
                    dialogHandler.showDialog(dialog)
                }

                override fun pasteToClipboard(text: String) {
                    val selection = StringSelection(text)
                    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                    clipboard.setContents(selection, selection)
                }

                override fun runErgoPayAction(action: ErgoPayAction) {
                    showDialog(
                        MosaikDialog(
                            "An ErgoPay action should be run:\n${action.url}",
                            "Ok, was done!",
                            "Cancel",
                            { action.onFinished?.let { runAction(it) } },
                            null
                        )
                    )
                }

                override fun runErgoAuthAction(action: ErgoAuthAction) {
                    showDialog(
                        MosaikDialog(
                            "An ErgoAuth action should be run:\n${action.url}",
                            "Ok, was done!",
                            "Cancel",
                            { action.onFinished?.let { runAction(it) } },
                            null
                        )
                    )
                }

                override fun openBrowser(url: String) {
                    val osName by lazy(LazyThreadSafetyMode.NONE) {
                        System.getProperty("os.name").lowercase(Locale.getDefault())
                    }
                    val desktop = Desktop.getDesktop()
                    when {
                        Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE) -> {
                            desktop.browse(URI(url))
                        }
                        "mac" in osName -> {
                            Runtime.getRuntime().exec("open $url")
                        }
                        "nix" in osName || "nux" in osName -> {
                            Runtime.getRuntime().exec("xdg-open $url")
                        }
                        else -> {
                            showDialog(
                                MosaikDialog(
                                    url, "OK",
                                    null,
                                    null,
                                    null
                                )
                            )
                        }
                    }
                }

                override val fiatRate = 2.5

                override fun convertErgToFiat(
                    nanoErg: Long,
                    withCurrency: Boolean
                ): String? {
                    return (if (withCurrency) "$" else "") +
                            BigDecimal(ErgoAmount(nanoErg).toDouble() * fiatRate).setScale(
                        2,
                        RoundingMode.HALF_UP
                    ).toPlainString()
                }

                override var preferFiatInput: Boolean = true

                override fun isErgoAddressValid(ergoAddress: String): Boolean {
                    // this is just for the desktop demo...
                    return ergoAddress.startsWith('9') || ergoAddress.startsWith('3')
                }

                override fun getErgoAddressLabel(ergoAddress: String): String? {
                    return if (ergoAddress.startsWith('9'))
                        "Mainnet $ergoAddress"
                    else if (ergoAddress.startsWith('3'))
                        "Testnet $ergoAddress"
                    else null
                }

                override fun getErgoWalletLabel(firstAddress: String): String? =
                    getErgoAddressLabel(firstAddress)

                override fun formatString(string: StringConstant, values: String?): String {
                    return when (string) {
                        StringConstant.ChooseAddress -> "Choose an address..."
                        StringConstant.PleaseChoose -> "Please choose an option"
                        StringConstant.ChooseWallet -> "Choose a wallet..."
                    }
                }

                override fun showErgoAddressChooser(valueId: String) {
                    TextInputDialog.showInputDialog(
                        { newAddress -> setValue(valueId, newAddress) },
                        "Address chooser",
                        "",
                        "Enter an Ergo address here.\nYes, this is ugly but only for debugging. :-)"
                    )
                }

                override fun showErgoWalletChooser(valueId: String) {
                    TextInputDialog.showInputDialog(
                        { addresses -> setValue(valueId, addresses?.split(',')) },
                        "Wallet chooser",
                        "",
                        "Enter a list of Ergo addresses (comma separated) here.\nYes, this is ugly but only for debugging. :-)"
                    )
                }

                override fun scanQrCode(actionId: String) {
                    TextInputDialog.showInputDialog(
                        { scannedText ->
                            scannedText?.let { qrCodeScanned(actionId, scannedText) }
                        },
                        "QR code scanner",
                        "",
                        "Enter the QR code contents here, no real scan for debugging purpose"
                    )
                }

                override fun runTokenInformationAction(action: TokenInformationAction) {
                    openBrowser("https://explorer.ergoplatform.com/en/token/${action.tokenId}")
                }
            }

        runtime.appLoaded = { manifestState.value = it }
        runtime.loadMosaikApp("")

        MosaikComposeConfig.DropDownMenu = { expanded,
                                             dismiss,
                                             modifier,
                                             content ->
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = dismiss,
                modifier = modifier,
                content = content,
            )
        }

        MosaikComposeConfig.DropDownItem = { onClick, content ->
            DropdownMenuItem(onClick = onClick, content = content)
        }

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
                TextFieldValue(runtime.appUrl ?: "", selection = TextRange(0, 1000))
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
            leadingIcon = {
                IconButton(
                    onClick = { runtime.navigateBack() },
                    enabled = runtime.canNavigateBack()
                ) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            },
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
        Text(
            key + ": " + value.inputValue.toString() +
                    " (" + (value.inputValue?.javaClass?.simpleName ?: "no type") + ", " +
                    (if (value.valid) "valid" else "invalid") + ")"
        )
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