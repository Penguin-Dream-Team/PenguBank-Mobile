package club.pengubank.mobile.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.InternalInteropApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import club.pengubank.mobile.R
import club.pengubank.mobile.security.SecurityUtils
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.services.SetupService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.storage.UserDataService
import club.pengubank.mobile.theme.PenguBankTheme
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

    private var navController: NavHostController? = null

    private var enablingBluetooth = false

    @SuppressLint("RestrictedApi", "SourceLockedOrientationActivity")
    @RequiresApi(Build.VERSION_CODES.P)
    @InternalInteropApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val keyGuardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        SecurityUtils.init()
        BiometricUtils.init(applicationContext, mainExecutor, keyGuardManager)

        val startDestination = if (storeState.hasPerformedSetup) "login" else "setup"

        requestPermission(Manifest.permission.CAMERA)
        requestPermission(Manifest.permission.BLUETOOTH)
        requestPermission(Manifest.permission.BLUETOOTH_ADMIN)

        setContent {
            val navController = rememberNavController()
            this.navController = navController

            PenguBankTheme {
                Scaffold(topBar = { TopBar(navController, storeState) }) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        if (!storeState.hasPerformedSetup) {
                            composable("setup") {
                                window.statusBarColor = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.statusBarDark
                                )
                                navController.backStack.clear()
                                SetupScreen(navController, setupService)
                            }
                        }

                        var loginEntry: NavBackStackEntry? = null

                        composable("login") {
                            loginEntry = it
                            window.statusBarColor =
                                ContextCompat.getColor(applicationContext, R.color.statusBarDark)
                            navController.backStack.clear()
                            navController.backStack.push(it)
                            LoginScreen(navController, loginService)
                        }

                        composable("dashboard") {
                            window.statusBarColor =
                                ContextCompat.getColor(applicationContext, R.color.statusBarLight)

                            navController.backStack.push(loginEntry!!)

                            DashboardScreen(
                                navController,
                                storeState,
                                this@MainActivity::requestBluetooth,
                                applicationContext
                            )
                        }

                        composable("camera") {
                            window.statusBarColor =
                                ContextCompat.getColor(applicationContext, R.color.statusBarDark)
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

    override fun onPause() {
        super.onPause()

        if (enablingBluetooth)
            return

        if (storeState.loggedIn)
            storeState.logout()
    }

    override fun onResume() {
        super.onResume()

        if (enablingBluetooth) {
            enablingBluetooth = false
            return
        }

        if (storeState.loggedIn)
            storeState.logout()
        if (storeState.hasPerformedSetup)
            navController?.navigate("login")
        else
            navController?.navigate("setup")
    }

    private fun requestBluetooth() {
        enablingBluetooth = true
        startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
            storeState.operation = "QRCode"
            navController?.navigate("camera")
        }
    }

    private fun requestPermission(permission: String) {
        if (PermissionChecker.checkSelfPermission(
                applicationContext,
                permission
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 100)
        }
    }
}
