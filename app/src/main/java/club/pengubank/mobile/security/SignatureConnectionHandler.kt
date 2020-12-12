package club.pengubank.mobile.security

import java.security.PublicKey

class SignatureConnectionHandler(private val desktopPublicKey: PublicKey) {

    fun signData(data: String): String = SecurityUtils.signData(data)

    fun verifySignature(data: String, signature: String): Boolean =
        SecurityUtils.verifySignature(data, signature, desktopPublicKey)
}