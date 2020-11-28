package club.pengubank.mobile.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.views.components.TopBar
import club.pengubank.mobile.views.dashboard.DashboardScreen
import club.pengubank.mobile.views.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var store: StoreState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }
}
