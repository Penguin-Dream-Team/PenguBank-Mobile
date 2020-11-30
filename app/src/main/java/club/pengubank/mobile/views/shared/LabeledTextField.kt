package club.pengubank.mobile.views.shared

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun LabeledTextField(
    value: MutableState<String>,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: VectorAsset? = null
) =
    TextField(
        value = value.value,
        onValueChange = { value.value = it },
        label = { Text(label) },
        leadingIcon = { if (icon != null) Icon(asset = icon) },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
