package org.ergoplatform.mosaik

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

/**
 * MosaikComposeDialog is an optional component when message dialogs should be handled by
 * Compose. [MosaikComposeDialogHandler] used here is to be used for [MosaikRuntime.showDialog].
 *
 * Alternatively, you can use your platform's native dialog system to show the message dialog.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MosaikComposeDialog(dialog: MosaikComposeDialogHandler) {
    val dialogState = dialog.flow.collectAsState()

    dialogState.value?.let { mosaikDialog ->
        AlertDialog(
            onDismissRequest = { dialog.dismiss() },
            text = {
                Text(
                    mosaikDialog.message,
                    Modifier.widthIn(300.dp),
                    style = labelStyle(LabelStyle.BODY1),
                    color = MosaikStyleConfig.defaultLabelColor
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialog.dismiss()
                        mosaikDialog.positiveButtonClicked?.run()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MosaikStyleConfig.textButtonTextColor),
                ) {
                    Text(
                        mosaikDialog.positiveButtonText,
                        style = labelStyle(LabelStyle.BODY1),
                    )
                }
            },
            dismissButton = {
                mosaikDialog.negativeButtonText?.let { buttonText ->
                    TextButton(
                        onClick = {
                            dialog.dismiss()
                            mosaikDialog.negativeButtonClicked?.run()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MosaikStyleConfig.textButtonTextColor),
                    ) {
                        Text(
                            buttonText,
                            style = labelStyle(LabelStyle.BODY1),
                        )
                    }
                }
            }
        )
    }
}

/**
 * Links [MosaikRuntime] with [MosaikComposeDialog]. Use [MosaikComposeDialogHandler.showDialog]
 * for [MosaikRuntime] and pass this object to [MosaikComposeDialog].
 */
class MosaikComposeDialogHandler {
    private val _stateFlow = MutableStateFlow<MosaikDialog?>(null)
    val flow: StateFlow<MosaikDialog?> get() = _stateFlow

    val showDialog: ((MosaikDialog) -> Unit) get() = { _stateFlow.value = it }

    fun dismiss() {
        _stateFlow.value = null
    }
}