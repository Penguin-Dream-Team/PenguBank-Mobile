package club.pengubank.mobile.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import club.pengubank.mobile.api.PenguBankApi
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.states.StoreState
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject
import kotlin.String as String

@Composable
fun topBar() {
    TopAppBar(title = { Text("Pengubank") }, actions = {})
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var store: StoreState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(topBar = { topBar() }) {
                MaterialTheme {
                    LoginScreen(loginService, store)
                }
            }
        }
    }
}

@Composable
fun LoginForm(loginState: LoginScreenState, store: StoreState) {
    val uiState = loginState.state

    if (uiState == LoginScreenState.LoginUIState.Success) {
        Text(text = store.token)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = loginState.email, onValueChange = { loginState.email = it })
            TextField(value = loginState.password, onValueChange = { loginState.password = it })
            Button(
                onClick = { loginState.login() },
                enabled = uiState != LoginScreenState.LoginUIState.Loading
            ) {
                Text("Login")
            }
            if (uiState is LoginScreenState.LoginUIState.Error) {
                Text(uiState.message)
            }
        }
    }
}

@Composable
fun LoginScreen(loginService: LoginService, store: StoreState) {
    LoginForm(LoginScreenState(loginService), store)
}
