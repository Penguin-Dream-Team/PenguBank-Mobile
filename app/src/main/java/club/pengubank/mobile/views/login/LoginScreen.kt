package club.pengubank.mobile.views.login

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.utils.biometric.BiometricUtils
import club.pengubank.mobile.views.login.partials.FingerprintButton
import club.pengubank.mobile.views.login.partials.LoginForm
import club.pengubank.mobile.views.shared.PenguLogoWithTitle

@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun LoginScreen(navController: NavController, loginService: LoginService) {
    val loginStoreState by remember { mutableStateOf(LoginScreenState(loginService)) }

    if (loginStoreState.state is LoginScreenState.LoginState.Success)
        navController.navigate("dashboard")
    else
        loginService.logout()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        PenguLogoWithTitle(modifier = Modifier.padding(top = 64.dp, bottom = 48.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginForm(loginStoreState)

            if (BiometricUtils.checkBiometricSupport())
                FingerprintButton(loginStoreState)
        }
    }
}
