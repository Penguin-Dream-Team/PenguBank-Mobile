package club.pengubank.mobile.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import club.pengubank.mobile.bluetooth.models.messages.PendingTransactionOperation
import club.pengubank.mobile.security.SecurityUtils
import club.pengubank.mobile.security.SignatureConnectionHandler
import club.pengubank.mobile.states.StoreState
import io.ktor.utils.io.errors.*
import java.util.*

class Client(private val store: StoreState) : Thread() {
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private lateinit var socket: BluetoothSocket
    private lateinit var device: BluetoothDevice
    private lateinit var adapter: BluetoothAdapter
    private var communicationService: BluetoothCommunicationService? = null

    private val signatureConnectionHandler =
        SignatureConnectionHandler(
            SecurityUtils.getPrivateKey(),
            SecurityUtils.parsePublicKey(store.kPub)
        )

    init {
        try {
            adapter = BluetoothAdapter.getDefaultAdapter()
            adapter.cancelDiscovery()
            device = adapter.getRemoteDevice(store.bluetoothMac)
            socket = device.createRfcommSocketToServiceRecord(MY_UUID)
            socket.connect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            // start handshake mah friend
            val handshakeService =
                BluetoothDiffieHellmanHandshakeService(socket, signatureConnectionHandler)
            // handshake finished
            communicationService = BluetoothCommunicationService(
                socket,
                signatureConnectionHandler,
                handshakeService.secretKey
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun close() {
        try {
            socket.close()
        } catch (e: IOException) {
            // do nothing djone cenõu
        }
    }

    fun refreshPendingTransactions() {
        waitForConnection()
        store.queuedTransactions = communicationService!!.refreshPendingTransactions()
    }

    private fun waitForConnection() {
        while(communicationService == null) { sleep(100) }
    }

    fun approvePendingTransactions(transactionId: Int, token: String) {
        waitForConnection()
        communicationService!!.updateTransaction(
            transactionId,
            token,
            PendingTransactionOperation.APPROVE
        )
    }

    fun cancelPendingTransactions(transactionId: Int, token: String) {
        waitForConnection()
        communicationService!!.updateTransaction(
            transactionId,
            token,
            PendingTransactionOperation.CANCEL
        )
    }
}

