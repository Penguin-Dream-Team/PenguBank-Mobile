package club.pengubank.mobile.views.login.partials

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.utils.biometric.BiometricUtils
import club.pengubank.mobile.views.shared.PasswordField

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun LoginForm(loginState: LoginScreenState) {
    val state = loginState.state

    if(state == LoginScreenState.LoginState.Empty)
    // open by default
    if(BiometricUtils.checkBiometricSupport())
        BiometricUtils.checkFingerPrint(loginState)

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

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        Button(
            onClick = { BiometricUtils.checkFingerPrint(loginState) },
            enabled = BiometricUtils.checkBiometricSupport()
        ) {
            Text("Login with FingerPrint")
        }

        if (state is LoginScreenState.LoginState.Error)
            Text(state.message)
    }
}
