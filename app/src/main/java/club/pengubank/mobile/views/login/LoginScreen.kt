package club.pengubank.mobile.views.login

import androidx.compose.runtime.*
import androidx.navigation.NavController
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.views.login.partials.LoginForm

@Composable
fun LoginScreen(
    navController: NavController,
    loginService: LoginService,
) {
    val loginStoreState by remember { mutableStateOf(LoginScreenState(loginService)) }
    LoginForm(navController, loginStoreState)
}

