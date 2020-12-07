package club.pengubank.mobile.views.setup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import club.pengubank.mobile.states.setup.SetupPasscodeScreenState
import club.pengubank.mobile.states.setup.SetupScreenState
import club.pengubank.mobile.views.shared.PasswordField
import io.ktor.util.*

@KtorExperimentalAPI
@Composable
fun SetupPasscodeForm(setupState: SetupPasscodeScreenState) {
    val state = setupState.state.value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            Icon(asset = Icons.Outlined.Email)
            Text(setupState.email.value)
        }

        Divider(modifier = Modifier.padding(vertical = 5.dp))

        PasswordField(value = setupState.passcode, label = "Passcode", isNumber = true)
        PasswordField(value = setupState.passcodeConfirm, label = "Confirm Passcode", isNumber = true)

        Button(
            onClick = { setupState.registerPasscode() },
            enabled = state != SetupScreenState.SetupState.PasscodeState.Loading
        ) { Text("Set Passcode") }

        if (state is SetupScreenState.SetupState.PasscodeState.Error)
            Text(state.message)
    }
}
