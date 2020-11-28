package club.pengubank.mobile.views.login.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import club.pengubank.mobile.states.LoginScreenState

@Composable
fun LoginForm(
    navController: NavController,
    loginState: LoginScreenState
) {
    val state = loginState.state

    if (state == LoginScreenState.LoginState.Success) {
        navController.navigate("dashboard")
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = loginState.email, onValueChange = { loginState.email = it })
            TextField(value = loginState.password, onValueChange = { loginState.password = it })

            Button(
                onClick = { loginState.login() },
                enabled = state != LoginScreenState.LoginState.Loading
            ) {
                Text("Login")
            }

            if (state is LoginScreenState.LoginState.Error) {
                Text(state.message)
            }
        }
    }
}
