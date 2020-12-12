package club.pengubank.mobile.bluetooth

import android.util.Log
import club.pengubank.mobile.bluetooth.models.BluetoothMessage
import club.pengubank.mobile.bluetooth.models.JSONObject
import club.pengubank.mobile.bluetooth.models.Response
import club.pengubank.mobile.bluetooth.models.toObject
import club.pengubank.mobile.errors.BluetoothMessageSignatureFailedException
import club.pengubank.mobile.security.SecurityUtils
import club.pengubank.mobile.security.SignatureConnectionHandler
import java.io.DataInputStream
import java.io.DataOutputStream
import javax.crypto.SecretKey

abstract class BluetoothService(
    protected val inputStream: DataInputStream, private val outputStream: DataOutputStream,
    protected val securityConnection: SignatureConnectionHandler
) {

    protected fun <T : JSONObject> sendMessage(data: T) {
        val message = BluetoothMessage(data.toJSON(), securityConnection.signData(data.toJSON()))
        outputStream.writeUTF(message.toJSON())
        outputStream.flush()
        Log.i("BLUE SENT", message.data)
    }

    protected fun <T : JSONObject> cipherAndSendMessage(secretKey: SecretKey, data: T) {
        val (cipheredData, parameterSpec) = SecurityUtils.cipher(secretKey, data.toJSON())
        val message = BluetoothMessage(
            cipheredData,
            securityConnection.signData(data.toJSON()),
            iv = parameterSpec.iv,
            tLen = parameterSpec.tLen
        )
        outputStream.writeUTF(message.toJSON())
        outputStream.flush()
    }

    protected inline fun <reified T : Response> receiveAndDecipherMessage(secretKey: SecretKey): Response {
        val message = inputStream.readUTF().toObject<BluetoothMessage>()

        val data = SecurityUtils.decipher(secretKey, message.data, message.iv!!, message.tLen!!)

        if (!securityConnection.verifySignature(data, message.signature))
            throw BluetoothMessageSignatureFailedException

        // expect to receive always
        val potentialError = data.toObject<Response.ErrorResponse>()

        if (potentialError.message == null)
            return data.toObject<T>()

        return potentialError
    }

    protected inline fun <reified T : JSONObject> receiveMessage(): T {
        val message = inputStream.readUTF().toObject<BluetoothMessage>()
        if (!securityConnection.verifySignature(message.data, message.signature))
            throw BluetoothMessageSignatureFailedException

        Log.i("BLUE RECEIVED", message.data)

        return message.data.toObject()
    }
}

