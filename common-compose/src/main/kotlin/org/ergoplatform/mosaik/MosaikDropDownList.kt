package org.ergoplatform.mosaik

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import org.ergoplatform.mosaik.model.ui.input.DropDownList

@Composable
fun MosaikDropDownList(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as DropDownList

    Column(modifier.fillMaxWidth()) {
        // Declaring a boolean value to store
        // the expanded state of the Text Field
        var expanded by remember { mutableStateOf(false) }

        // This value is used to assign to the DropDown the same width
        var textFieldSize by remember { mutableStateOf(Size.Zero) }

        // Up Icon when expanded and down icon when collapsed
        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        // Create an Outlined Text Field
        // with icon and not expanded
        OutlinedTextField(
            value = treeElement.currentValueAsString,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { if (element.isEnabled) expanded = !expanded },
                    indication = null
                ),
            label = { },
            enabled = false,
            maxLines = 1,
            singleLine = true,
            readOnly = true,
            trailingIcon = { Icon(icon, null) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = if (element.isEnabled) MosaikStyleConfig.defaultLabelColor else MosaikStyleConfig.secondaryLabelColor,
                cursorColor = MosaikStyleConfig.defaultLabelColor,
                disabledTrailingIconColor = MosaikStyleConfig.secondaryLabelColor,
                disabledBorderColor = if (element.isEnabled) MosaikStyleConfig.defaultLabelColor else MosaikStyleConfig.secondaryLabelColor
            )
        )

        MosaikComposeConfig.DropDownMenu(
            expanded,
            { expanded = false },
            Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
        ) {
            element.entries.forEach { (key, value) ->
                MosaikComposeConfig.DropDownItem({
                    //selectedItem = key
                    expanded = false
                    treeElement.changeValueFromInput(key)
                }) {
                    Text(text = value)
                }
            }
        }
    }
}

