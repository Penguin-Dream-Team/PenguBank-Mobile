package club.pengubank.mobile.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ContextAmbient
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
import club.pengubank.mobile.storage.UserDataService
import club.pengubank.mobile.utils.camera.Camera
import dagger.hilt.android.AndroidEntryPoint
import club.pengubank.mobile.views.dashboard.DashboardScreen
import club.pengubank.mobile.views.login.LoginScreen
import club.pengubank.mobile.views.setup.SetupScreen
import club.pengubank.mobile.views.shared.TopBar
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

    @Inject
    lateinit var userDataService: UserDataService

    @SuppressLint("WrongConstant")
    @InternalInteropApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val MY_CAMERA_REQUEST_CODE = 100

        setContent {
            val navController = rememberNavController()

            val startDestination = if (storeState.hasPerformedSetup) "login" else "setup"
            if (PermissionChecker.checkSelfPermission(ContextAmbient.current, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
            }

            MaterialTheme {
                Scaffold(topBar = { TopBar(navController, storeState) }) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("setup") { SetupScreen(navController, setupService) }

                        composable("login") { LoginScreen(navController, loginService) }
                        composable("dashboard") { DashboardScreen(navController, transactionService, storeState) }

                        composable("camera") { Camera().SimpleCameraPreview(navController, storeState, setupService) }
                    }
                }
            }
        }
    }
}



