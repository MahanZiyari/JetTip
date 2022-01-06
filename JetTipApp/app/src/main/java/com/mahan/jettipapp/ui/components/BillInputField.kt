package com.mahan.jettipapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: String,
    onValueChanged: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    isSingleLine: Boolean = true,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState,
        onValueChange = {
            onValueChanged(it)
        },
        modifier = modifier.padding(8.dp),
        label = {
            Text(text = label)
        },
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = "Money")
        },
        singleLine = isSingleLine,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        ),
        keyboardActions = onAction
    )
}