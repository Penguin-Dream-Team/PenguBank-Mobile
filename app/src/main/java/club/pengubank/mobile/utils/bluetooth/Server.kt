package club.pengubank.mobile.utils.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Message
import io.ktor.utils.io.errors.*

class Server() : Thread() {
    private var bluetooth = Bluetooth()
    private var serverSocket: BluetoothServerSocket? = null

    override fun run() {
        var socket: BluetoothSocket? = null
        while (socket == null) {
            try {
                val message: Message = Message.obtain()
                message.what = bluetooth.STATE_CONNECTING
                //handler.sendMessage(message)
                socket = serverSocket!!.accept()
            } catch (e: IOException) {
                e.printStackTrace()
                val message: Message = Message.obtain()
                message.what = bluetooth.STATE_CONNECTION_FAILED
                //handler.sendMessage(message)
            }
            if (socket != null) {
                val message: Message = Message.obtain()
                message.what = bluetooth.STATE_CONNECTED
                //handler.sendMessage(message)
                val sendReceive = SendReceive(socket)
                sendReceive.start()
                break
            }
        }
    }

    init {
        try {
            serverSocket = bluetooth.bluetoothAdapter!!.listenUsingRfcommWithServiceRecord(bluetooth.APP_NAME, bluetooth.MY_UUID)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}