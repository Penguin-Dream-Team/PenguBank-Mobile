package club.pengubank.mobile.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import club.pengubank.mobile.bluetooth.models.messages.PendingTransactionOperation
import club.pengubank.mobile.errors.BluetoothErrorResponseException
import club.pengubank.mobile.security.SecurityUtils
import club.pengubank.mobile.security.SignatureConnectionHandler
import club.pengubank.mobile.states.StoreState
import io.ktor.utils.io.errors.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class Client(private val store: StoreState) : Thread() {
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private lateinit var socket: BluetoothSocket
    private lateinit var device: BluetoothDevice
    private lateinit var adapter: BluetoothAdapter
    private var communicationService: BluetoothCommunicationService? = null

    private var inputStream: DataInputStream? = null
    private var outputStream: DataOutputStream? = null

    private val signatureConnectionHandler =
        SignatureConnectionHandler(SecurityUtils.parsePublicKey(store.kPub))

    init {
        try {
            adapter = BluetoothAdapter.getDefaultAdapter()
            adapter.cancelDiscovery()
            device = adapter.getRemoteDevice(store.bluetoothMac)
            socket = device.createRfcommSocketToServiceRecord(MY_UUID)
            socket.connect()
            inputStream = DataInputStream(socket.inputStream)
            outputStream = DataOutputStream(socket.outputStream)
        } catch (e: IOException) {
            close()
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            // start handshake mah friend
            val handshakeService =
                BluetoothDiffieHellmanHandshakeService(
                    inputStream!!,
                    outputStream!!,
                    signatureConnectionHandler
                )
            // handshake finished
            communicationService = BluetoothCommunicationService(
                inputStream!!, outputStream!!,
                signatureConnectionHandler,
                handshakeService.secretKey
            )
        } catch (e: IOException) {
            close()
            e.printStackTrace()
        }
    }

    fun close() {
        try {
            store.qrcodeScanned = false
            store.client = null
            inputStream?.close()
            outputStream?.close()
            socket.close()
        } catch (e: IOException) {
            // do nothing djone cenõu
        }
    }

    fun refreshPendingTransactions() {
        try {
            waitForConnection()
            store.queuedTransactions = communicationService!!.refreshPendingTransactions()
        } catch (e: IOException) {
            close()
            throw BluetoothErrorResponseException("Bluetooth connection lost")
        }
    }

    private fun waitForConnection() {
        while (communicationService == null) {
            sleep(100)
        }
    }

    fun approvePendingTransactions(transactionId: Int, token: String) {
        try {
            waitForConnection()
            communicationService!!.updateTransaction(
                transactionId,
                token,
                PendingTransactionOperation.APPROVE
            )
        } catch (e: IOException) {
            close()
            throw BluetoothErrorResponseException("Bluetooth connection lost")
        }
    }

    fun cancelPendingTransactions(transactionId: Int, token: String) {
        try {
            waitForConnection()
            communicationService!!.updateTransaction(
                transactionId,
                token,
                PendingTransactionOperation.CANCEL
            )
        } catch (e: IOException) {
            close()
            throw BluetoothErrorResponseException("Bluetooth connection lost")
        }
    }
}

