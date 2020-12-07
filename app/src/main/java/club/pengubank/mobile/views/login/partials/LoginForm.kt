package club.pengubank.mobile.views.login.partials

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
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.views.shared.PasswordField

@Composable
fun LoginForm(loginState: LoginScreenState) {
    val state = loginState.state

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Icon(asset = Icons.Outlined.Email)
            Text(loginState.email)
        }

        Divider(modifier = Modifier.padding(vertical = 5.dp))

        PasswordField(value = loginState.passcode, label = "Passcode", isNumber = true)

        Button(
            onClick = { loginState.login() },
            enabled = state != LoginScreenState.LoginState.Loading
        ) {
            Text("Login")
        }

        if (state is LoginScreenState.LoginState.Error)
            Text(state.message)
    }
}
