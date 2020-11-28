package club.pengubank.mobile.views.dashboard.partials.totp

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.utils.totp.TOTPAuthenticator
import club.pengubank.mobile.utils.totp.TOTPConfig
import club.pengubank.mobile.views.components.IconButton
import club.pengubank.mobile.views.dashboard.partials.TransactionSection
import club.pengubank.mobile.views.dashboard.partials.UserInfo
import java.util.*

@Composable
fun TOTPCodeBar(store: StoreState) {
    var visible by remember { mutableStateOf(false) }
    val code by remember { mutableStateOf(store.validationCode) }

    Row(
        modifier = Modifier
            .preferredHeight(56.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (visible) code.toString() else "xxx xxx",
            fontSize = 32.sp,
            modifier = Modifier.weight(2.0f, true)
        )

        if (visible.not()) {
            ShowCodeButton { visible = true }
        } else {
            HideCodeButton { visible = false }
        }
    }
}

@Composable
private fun ShowCodeButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        icon = Icons.Outlined.Visibility,
        description = "View TOTP",
        selected = false
    )
}

@Composable
private fun HideCodeButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        icon = Icons.Outlined.VisibilityOff,
        description = "Hide TOTP",
        selected = true
    )
}
