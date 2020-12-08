package club.pengubank.mobile.views

import android.Manifest
import android.app.KeyguardManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.InternalInteropApi
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import club.pengubank.mobile.security.SecurityUtils
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.services.SetupService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.storage.UserDataService
import club.pengubank.mobile.utils.biometric.BiometricUtils
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
    lateinit var userDataService: UserDataService

    @Inject
    lateinit var storeState: StoreState

    private lateinit var navController: NavHostController

    @RequiresApi(Build.VERSION_CODES.P)
    @InternalInteropApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyGuardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        SecurityUtils.init()
        BiometricUtils.init(this, mainExecutor, keyGuardManager)

        setContent {
            navController = rememberNavController()
            val startDestination = if (storeState.hasPerformedSetup) "login" else "setup"
            if (PermissionChecker.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.CAMERA
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            }

            MaterialTheme {
                Scaffold(topBar = { TopBar(navController, storeState) }) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("setup") { SetupScreen(navController, setupService) }

                        composable("login") { LoginScreen(navController, loginService) }
                        composable("dashboard") {
                            DashboardScreen(
                                navController,
                                storeState,
                                this@MainActivity::requestBluetooth,
                                applicationContext
                            )
                        }

                        composable("camera") {
                            Camera().SimpleCameraPreview(
                                navController,
                                storeState,
                                setupService
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestBluetooth() {
        startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
            storeState.operation = "QRCode"
            navController.navigate("camera")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}



