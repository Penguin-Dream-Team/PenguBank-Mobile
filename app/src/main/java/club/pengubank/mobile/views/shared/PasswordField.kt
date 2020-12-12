package club.pengubank.mobile.views.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun PasswordField(
    value: MutableState<String>,
    label: String,
    isNumber: Boolean = false
) =
    LabeledTextField(
        value = value,
        label = label,
        icon = Icons.Outlined.Lock,
        visualTransformation = PasswordVisualTransformation(),
        keyboardType = if(isNumber) KeyboardType.NumberPassword else KeyboardType.Password,
    )
