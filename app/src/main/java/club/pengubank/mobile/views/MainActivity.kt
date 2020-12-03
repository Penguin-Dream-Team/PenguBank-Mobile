package club.pengubank.mobile.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.InternalInteropApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.services.SetupService
import club.pengubank.mobile.services.TransactionService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.views.dashboard.DashboardScreen
import club.pengubank.mobile.views.login.LoginScreen
import club.pengubank.mobile.views.setup.SetupScreen
import club.pengubank.mobile.views.shared.TopBar
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.*
import javax.inject.Inject


@KtorExperimentalAPI
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var setupService: SetupService

    @Inject
    lateinit var transactionService: TransactionService

    @Inject
    lateinit var storeState: StoreState
    
    @InternalInteropApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val startDestination = if (storeState.hasPerformedSetup) "login" else "setup"

            MaterialTheme {
                Scaffold(topBar = { TopBar(navController, storeState) }) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("setup") { SetupScreen(navController, setupService) }

                        composable("login") { LoginScreen(navController, loginService) }
                        composable("dashboard") { DashboardScreen(navController, transactionService, storeState) }
                    }
                }
            }
        }
    }
}
