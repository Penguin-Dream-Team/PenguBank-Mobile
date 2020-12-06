package club.pengubank.mobile.utils.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

import android.bluetooth.BluetoothSocket
import android.os.Message
import android.util.Log
import io.ktor.utils.io.errors.*
import java.util.*

class Client(address: String) : Thread() {
    val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private lateinit var socket: BluetoothSocket
    private lateinit var device: BluetoothDevice
    private lateinit var adapter: BluetoothAdapter

    init {
        try {
            adapter = BluetoothAdapter.getDefaultAdapter()
            adapter.cancelDiscovery()
            device = adapter.getRemoteDevice(address)
            println(device.bondState)
            //Log.i("BLUE", device.name)
            socket = device.createRfcommSocketToServiceRecord(MY_UUID)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            socket!!.connect()
            val inputStream = socket!!.inputStream.bufferedReader()
            val outputStream = socket!!.outputStream.bufferedWriter()

            outputStream.write("Hello there")
            outputStream.newLine()
            outputStream.flush()
            val received = inputStream.readLine()
            Log.i("BLUUETOOO", received)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}