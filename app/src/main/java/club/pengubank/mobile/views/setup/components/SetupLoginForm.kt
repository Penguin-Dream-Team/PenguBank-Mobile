package club.pengubank.mobile.views.setup.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.states.setup.SetupInitScreenState
import club.pengubank.mobile.states.setup.SetupScreenState
import club.pengubank.mobile.views.shared.ErrorStatus
import club.pengubank.mobile.views.shared.LabeledTextField
import club.pengubank.mobile.views.shared.PasswordField
import io.ktor.util.*

@KtorExperimentalAPI
@Composable
fun SetupLoginForm(setupState: SetupInitScreenState) {
    val state = setupState.state.value

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LabeledTextField(
            value = setupState.email,
            label = "Email",
            icon = Icons.Outlined.Email,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.preferredHeight(16.dp))

        PasswordField(value = setupState.password, label = "Password")

        Spacer(modifier = Modifier.preferredHeight(22.dp))

        Button(
            onClick = { setupState.login() },
            enabled = state != SetupScreenState.SetupState.InitState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Setup") }

        Spacer(modifier = Modifier.preferredHeight(22.dp))

        if (state is SetupScreenState.SetupState.InitState.Error)
            ErrorStatus(state.message)
    }
}
