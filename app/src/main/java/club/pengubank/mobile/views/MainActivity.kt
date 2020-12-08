package club.pengubank.mobile.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.InternalInteropApi
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.services.SetupService
import club.pengubank.mobile.services.TransactionService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.utils.camera.Camera
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
            if (PermissionChecker.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            }

            MaterialTheme {
                Scaffold(topBar = { TopBar(navController, storeState) }) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("setup") { SetupScreen(navController, setupService) }

                        composable("login") { LoginScreen(navController, loginService) }
                        composable("dashboard") { DashboardScreen(navController, transactionService, storeState) }

                        composable("camera") { Camera().SimpleCameraPreview(navController, storeState) }
                    }
                }
            }
        }
    }
}



