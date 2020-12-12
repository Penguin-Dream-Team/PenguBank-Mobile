package club.pengubank.mobile.views.dashboard.partials.qrcode

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import club.pengubank.mobile.states.StoreState

@Composable
fun QRCodeScan(navController: NavController, storeState: StoreState, requestBluetooth: () -> Unit) {
    Row(
        modifier = Modifier
            .preferredHeight(56.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
                    requestBluetooth()
                } else {
                    storeState.operation = "QRCode"
                    navController.navigate("camera")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight()
        ) {
            Text(
                text = "Scan QR Code",
                textAlign = TextAlign.Center,
            )
        }
    }
}
