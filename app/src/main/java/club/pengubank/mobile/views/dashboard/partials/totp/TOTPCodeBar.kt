package club.pengubank.mobile.views.dashboard.partials.totp

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.animate
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Duration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.inMilliseconds
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.InternalInteropApi
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.utils.totp.TOTPAuthenticator
import club.pengubank.mobile.utils.totp.TOTPSecretKey
import club.pengubank.mobile.views.shared.IconButton
import java.time.Instant

private fun genCode(storeState: StoreState): String {
    val secretKey = TOTPSecretKey.from(value = storeState.totpSecretKey)
    return buildString {
        append(
            TOTPAuthenticator().calculateLastValidationCode(secretKey.value).toString()
                .padStart(6, '0')
        ).insert(3, ' ')
    }
}

val TIME_WINDOW = Duration(seconds = 30).inMilliseconds()

class CodeTask(
    private val mainHandler: Handler,
    private val progress: MutableState<Long>,
    private val code: MutableState<String>,
    private val storeState: StoreState
) : Runnable {
    override fun run() {
        val oldProgress = progress.value
        progress.value = Instant.now().toEpochMilli() % TIME_WINDOW
        if (oldProgress > progress.value)
            code.value = genCode(storeState)
    }
}

@InternalInteropApi
@Composable
fun ProgressCircle(progress: Float) {
    val animatedProgress = animate(
        target = progress,
        animSpec = ProgressIndicatorConstants.DefaultProgressAnimationSpec
    )

    CircularProgressIndicator(
        progress = 1 - animatedProgress / TIME_WINDOW.toFloat(),
        modifier = Modifier.padding(end = 10.dp),
        strokeWidth = 25.dp
    )
}


@InternalInteropApi
@Composable
fun TOTPCodeBar(store: StoreState) {
    val mainHandler by remember { mutableStateOf(Handler(Looper.getMainLooper())) }
    val code = remember { mutableStateOf(genCode(store)) }
    val progress = mutableStateOf(Instant.now().toEpochMilli() % TIME_WINDOW)

    Row(
        modifier = Modifier
            .preferredHeight(56.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = code.value,
            fontSize = 32.sp,
            modifier = Modifier.weight(2.0f, true)
        )

        mainHandler.postDelayed(CodeTask(mainHandler, progress, code, store), 50L)
        ProgressCircle(progress = progress.value.toFloat())
    }
}
