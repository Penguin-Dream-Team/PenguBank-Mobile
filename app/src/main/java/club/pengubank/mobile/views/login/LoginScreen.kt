package club.pengubank.mobile.views.login

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.views.login.partials.LoginForm

@SuppressLint("RestrictedApi")
@Composable
fun LoginScreen(navController: NavController, loginService: LoginService) {
    navController.backStack.clear()
    val loginStoreState by remember { mutableStateOf(LoginScreenState(loginService)) }

    if (loginStoreState.state is LoginScreenState.LoginState.Success)
        navController.navigate("dashboard")
    else
        loginService.logout()

    LoginForm(loginStoreState)

}

