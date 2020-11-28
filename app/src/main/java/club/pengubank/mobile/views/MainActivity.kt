package club.pengubank.mobile.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.utils.totp.TOTPAuthenticator
import club.pengubank.mobile.utils.totp.TOTPConfig
import club.pengubank.mobile.utils.totp.TOTPSecretKey
import club.pengubank.mobile.views.components.TopBar
import club.pengubank.mobile.views.dashboard.DashboardScreen
import club.pengubank.mobile.views.dashboard.partials.TransactionSection
import club.pengubank.mobile.views.dashboard.partials.UserInfo
import club.pengubank.mobile.views.dashboard.partials.totp.TOTPSection
import club.pengubank.mobile.views.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var store: StoreState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val timerTask = object : TimerTask() {
            override fun run() {
                store.validationCode = TOTPAuthenticator().calculateLastValidationCode(TOTPSecretKey.from(value = "XqbqvNi0aYIIGw==").value)
            }
        }
        store.validationCode = TOTPAuthenticator().calculateLastValidationCode(TOTPSecretKey.from(value = "XqbqvNi0aYIIGw==").value)

        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                Scaffold(topBar = { TopBar(navController, store) }) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController, loginService) }
                        composable("dashboard") { DashboardScreen(navController, store) }
                    }
                }
            }
        }

        val timer = Timer()
        timer.schedule(timerTask, TOTPAuthenticator().getTimeWindowFromTime(), TOTPConfig().timeStepSize.seconds)
    }
}
