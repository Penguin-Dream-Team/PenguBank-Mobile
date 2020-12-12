package club.pengubank.mobile.views.setup.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import club.pengubank.mobile.states.setup.SetupPasscodeScreenState
import club.pengubank.mobile.states.setup.SetupScreenState
import club.pengubank.mobile.views.shared.DisabledTextField
import club.pengubank.mobile.views.shared.ErrorStatus
import club.pengubank.mobile.views.shared.PasswordField
import io.ktor.util.*

@KtorExperimentalAPI
@Composable
fun SetupPasscodeForm(setupState: SetupPasscodeScreenState) {
    val state = setupState.state.value

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DisabledTextField(
            icon = Icons.Outlined.Email,
            label = "Email",
            value = setupState.email.value
        )

        Spacer(modifier = Modifier.preferredHeight(16.dp))

        PasswordField(value = setupState.passcode, label = "Passcode", isNumber = true)

        Spacer(modifier = Modifier.preferredHeight(16.dp))

        PasswordField(
            value = setupState.passcodeConfirm,
            label = "Confirm Passcode",
            isNumber = true
        )

        Spacer(modifier = Modifier.preferredHeight(22.dp))

        Button(
            onClick = { setupState.registerPasscode() },
            enabled = state != SetupScreenState.SetupState.PasscodeState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Set Passcode") }

        Spacer(modifier = Modifier.preferredHeight(22.dp))

        if (state is SetupScreenState.SetupState.PasscodeState.Error)
            ErrorStatus(state.message)
    }
}
