package club.pengubank.mobile.views.login.partials

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.utils.biometric.BiometricUtils
import club.pengubank.mobile.views.shared.DisabledTextField
import club.pengubank.mobile.views.shared.ErrorStatus
import club.pengubank.mobile.views.shared.PasswordField

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun LoginForm(loginState: LoginScreenState) {
    val state = loginState.state

    if (state == LoginScreenState.LoginState.Empty)

    // open by default
        if (BiometricUtils.checkBiometricSupport() && loginState.fingerprintAttempts < BiometricUtils.MAX_ATTEMPTS)
            BiometricUtils.checkFingerPrint(loginState)

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DisabledTextField(
            icon = Icons.Outlined.Email,
            label = "Email",
            value = loginState.email
        )

        Spacer(modifier = Modifier.preferredHeight(16.dp))

        PasswordField(value = loginState.passcode, label = "Passcode", isNumber = true)

        Spacer(modifier = Modifier.preferredHeight(22.dp))

        Button(
            onClick = { loginState.login() },
            enabled = state != LoginScreenState.LoginState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(text = "Login") }

        Spacer(modifier = Modifier.preferredHeight(22.dp))

        if (state is LoginScreenState.LoginState.Error)
            ErrorStatus(state.message)
    }
}
