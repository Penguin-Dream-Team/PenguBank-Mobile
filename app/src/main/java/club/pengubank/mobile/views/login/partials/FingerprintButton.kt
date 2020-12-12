package club.pengubank.mobile.views.login.partials

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.utils.biometric.BiometricUtils

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun FingerprintButton(loginStoreState: LoginScreenState) {
    Button(
        onClick = { BiometricUtils.checkFingerPrint(loginStoreState) },
        enabled = loginStoreState.fingerprintAttempts < BiometricUtils.MAX_ATTEMPTS,
        colors = ButtonConstants.defaultOutlinedButtonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(16.dp),
        elevation = ButtonConstants.defaultElevation(0.dp, 0.dp),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text(
            text = "Login with FingerPrint",
            textDecoration = TextDecoration.Underline
        )
    }
}
