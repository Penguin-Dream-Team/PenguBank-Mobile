package club.pengubank.mobile.bluetooth

import club.pengubank.mobile.bluetooth.models.messages.DiffieHellmanInitRequestMessage
import club.pengubank.mobile.bluetooth.models.messages.DiffieHellmanInitResponseMessage
import club.pengubank.mobile.errors.BluetoothMessageSignatureFailedException
import club.pengubank.mobile.security.DiffieHellmanUtils
import club.pengubank.mobile.security.SignatureConnectionHandler
import java.io.DataInputStream
import java.io.DataOutputStream
import javax.crypto.SecretKey

class BluetoothDiffieHellmanHandshakeService(
    inputStream: DataInputStream, outputStream: DataOutputStream,
    securityConnection: SignatureConnectionHandler,
) : BluetoothService(inputStream, outputStream, securityConnection) {

    lateinit var secretKey: SecretKey

    init {
        val (keyAgreement, keyPair) = DiffieHellmanUtils.init()
        var signatureFailed = false

        do {
            try {
                // receive diffie hellman public b
                val request = receiveMessage<DiffieHellmanInitRequestMessage>()
                val otherPublicKey = DiffieHellmanUtils.parsePublicKeyFromY(keyPair, request.publicY)

                // send diffie hellman public a
                val myPublicY = DiffieHellmanUtils.retrieveYFromPublicKey(keyPair)
                sendMessage(DiffieHellmanInitResponseMessage(myPublicY))

                // generate key
                secretKey = DiffieHellmanUtils.generateSecretKey(keyAgreement, otherPublicKey)
            } catch (e: BluetoothMessageSignatureFailedException) {
                // Do nothing, continue loop
                e.printStackTrace()
                signatureFailed = true
            }
        } while (signatureFailed)
    }
}