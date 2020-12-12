package club.pengubank.mobile.utils.biometric

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import club.pengubank.mobile.states.LoginScreenState
import java.util.concurrent.Executor

object BiometricUtils {
    private lateinit var context: Context
    private lateinit var executor: Executor
    private lateinit var keyGuardManager: KeyguardManager
    private lateinit var cancellationSignal: CancellationSignal
    private lateinit var loginState: LoginScreenState
    private lateinit var biometricPrompt: BiometricPrompt
    const val MAX_ATTEMPTS = 3

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)

                    if(loginState.fingerprintAttempts >= MAX_ATTEMPTS)
                        notifyUser("Failed after 3 attempts. Insert passcode")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    loginState.fingerprintAttempts++

                    if (loginState.fingerprintAttempts >= MAX_ATTEMPTS) {
                        cancellationSignal.cancel()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication success!")
                    loginState.biometricLogin()
                }
            }

    fun init(mainContext: Context, mainExecutor: Executor, mainKeyGuardManager: KeyguardManager) {
        context = mainContext
        executor = mainExecutor
        keyGuardManager = mainKeyGuardManager
    }

    fun checkBiometricSupport(): Boolean {
        if (!keyGuardManager.isKeyguardSecure) {
            notifyUser("Fingerprint authentication has not been enable in settings")
            return false
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }

        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun checkFingerPrint(state: LoginScreenState) {
        if (state.fingerprintAttempts >= MAX_ATTEMPTS) {
            notifyUser("Failed after 3 attempts. Insert passcode")
            return
        }

        loginState = state
        biometricPrompt = BiometricPrompt.Builder(context)
            .setTitle("Login using your fingerprint")
            .setSubtitle("Authentication is required")
            .setDescription("This app uses fingerprint protection to keep your data secure")
            .setNegativeButton("Cancel", executor, { _, _ ->
                notifyUser("Authentication cancelled")
            }).build()
        biometricPrompt.authenticate(getCancellationSignal(), executor, authenticationCallback)
    }

    private fun notifyUser(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            if (loginState.fingerprintAttempts < MAX_ATTEMPTS)
                notifyUser("Authentication was cancelled by the user")
        }

        return cancellationSignal
    }
}