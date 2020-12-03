package club.pengubank.mobile.utils.bluetooth

import android.bluetooth.BluetoothDevice

import android.bluetooth.BluetoothSocket
import android.os.Message
import io.ktor.utils.io.errors.*

class Client(device: BluetoothDevice) : Thread() {
    private var bluetooth = Bluetooth()
    private var socket: BluetoothSocket? = null

    override fun run() {
        try {
            socket!!.connect()
            val message: Message = Message.obtain()
            message.what = bluetooth.STATE_CONNECTED
            //handler.sendMessage(message)
            val sendReceive = SendReceive(socket!!)
            sendReceive.start()
        } catch (e: IOException) {
            e.printStackTrace()
            val message: Message = Message.obtain()
            message.what = bluetooth.STATE_CONNECTION_FAILED
            //handler.sendMessage(message)
        }
    }

    init {
        try {
            socket = device.createRfcommSocketToServiceRecord(bluetooth.MY_UUID)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}