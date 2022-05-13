package org.ergoplatform.mosaik

import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * MosaikComposeDialog is an optional component when message dialogs should be handled by
 * Compose. [MosaikComposeDialogHandler] used here is to be used for [ActionRunner.showDialog].
 *
 * Alternatively, you can use your platform's native dialog system to show the message dialog.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MosaikComposeDialog(dialog: MosaikComposeDialogHandler) {
    val dialogState = dialog.flow.collectAsState()
    // TODO use correct colors
    dialogState.value?.let { mosaikDialog ->
        AlertDialog(
            onDismissRequest = { dialog.dismiss() },
            text = {
                Text(mosaikDialog.message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mosaikDialog.positiveButtonClicked?.run()
                        dialog.dismiss()
                    }
                ) {
                    Text(mosaikDialog.positiveButtonText)
                }
            },
            dismissButton = {
                mosaikDialog.negativeButtonText?.let { buttonText ->
                    TextButton(
                        onClick = {
                            mosaikDialog.negativeButtonClicked?.run()
                            dialog.dismiss()
                        }
                    ) {
                        Text(buttonText)
                    }
                }
            }
        )
    }
}

/**
 * Links [ActionRunner] with [MosaikComposeDialog]. Use [MosaikComposeDialogHandler.showDialog]
 * for [ActionRunner] and pass this object to [MosaikComposeDialog].
 */
class MosaikComposeDialogHandler {
    private val _stateFlow = MutableStateFlow<MosaikDialog?>(null)
    val flow: StateFlow<MosaikDialog?> get() = _stateFlow

    val showDialog: ((MosaikDialog) -> Unit) get() = { _stateFlow.value = it }

    fun dismiss() {
        _stateFlow.value = null
    }
}