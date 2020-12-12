package club.pengubank.mobile.bluetooth

import club.pengubank.mobile.bluetooth.models.Response
import club.pengubank.mobile.bluetooth.models.messages.PendingTransaction
import club.pengubank.mobile.bluetooth.models.messages.PendingTransactionOperation
import club.pengubank.mobile.bluetooth.models.messages.RetrievePendingTransactionsRequest
import club.pengubank.mobile.bluetooth.models.messages.UpdatePendingTransactionRequest
import club.pengubank.mobile.errors.BluetoothErrorResponseException
import club.pengubank.mobile.security.SignatureConnectionHandler
import java.io.DataInputStream
import java.io.DataOutputStream
import java.time.Instant
import javax.crypto.SecretKey

class BluetoothCommunicationService(
    inputStream: DataInputStream, outputStream: DataOutputStream,
    private val signatureConnectionHandler: SignatureConnectionHandler,
    private val secretKey: SecretKey
) : BluetoothService(inputStream, outputStream, signatureConnectionHandler) {

    private val usedNonces = mutableListOf<Long>()

    fun refreshPendingTransactions(): List<PendingTransaction> {
        val request = RetrievePendingTransactionsRequest(generateNonce())
        cipherAndSendMessage(secretKey, request)

        val response = receiveAndDecipherMessage<Response.RetrievePendingTransactionsResponse>(secretKey)

        if (response is Response.ErrorResponse)
            throw BluetoothErrorResponseException(response.message!!)

        return (response as Response.RetrievePendingTransactionsResponse).pendingTransactions
    }

    fun updateTransaction(transactionId: Int, token: String, type: PendingTransactionOperation) {
        val signedToken = signatureConnectionHandler.signData(token)

        val request = UpdatePendingTransactionRequest(
            generateNonce(),
            transactionId,
            signedToken,
            type
        )

        cipherAndSendMessage(secretKey, request)

        val response = receiveAndDecipherMessage<Response.UpdatePendingTransactionResponse>(secretKey)

        if (response is Response.ErrorResponse)
            throw BluetoothErrorResponseException(response.message!!)
    }

    private fun generateNonce(): Long {
        var nonce: Long

        do {
            nonce = Instant.now().toEpochMilli()
        } while (usedNonces.contains(nonce))

        usedNonces.add(nonce)

        return nonce
    }
}