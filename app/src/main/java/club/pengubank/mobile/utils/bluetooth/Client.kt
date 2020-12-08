package club.pengubank.mobile.utils.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.google.gson.Gson
import io.ktor.utils.io.errors.*
import java.io.DataInputStream
import java.io.DataOutputStream
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
            socket = device.createRfcommSocketToServiceRecord(MY_UUID)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            socket.connect()

            val verificationBluetooth = VerificationBluetooth(socket)
            verificationBluetooth.sendTest(VerifyPublicKeyRequest("this is a public key"))
            val response = verificationBluetooth.receiveTest()
            println(response)

            verificationBluetooth.sendTest(VerifyPublicKeyRequest("this is a public key"))
            val response2 = verificationBluetooth.receiveTest()
            println(response2)

            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

inline fun <reified T : JSONObject> String.toObject(): T = Gson().fromJson(this, T::class.java)

interface JSONObject {
    fun toJSON(): String = Gson().toJson(this)
}

data class VerifyPublicKeyRequest(val pubKey: String) : JSONObject
data class VerifyPublicKeyResponse(val ok: Boolean) : JSONObject

abstract class BluetoothService(socket: BluetoothSocket) {
    protected val inputStream: DataInputStream = DataInputStream(socket.inputStream)
    private val outputStream: DataOutputStream = DataOutputStream(socket.outputStream)

    protected fun <T : JSONObject> sendMessage(data: T) {
        outputStream.writeUTF(data.toJSON())
        outputStream.flush()
    }
    protected inline fun <reified T : JSONObject> receiveMessage(): T =
        inputStream.readUTF().toObject()
}

class VerificationBluetooth(socket: BluetoothSocket): BluetoothService(socket) {
    fun sendTest(data: VerifyPublicKeyRequest) = sendMessage(data)
    fun receiveTest() = receiveMessage<VerifyPublicKeyResponse>()
}
