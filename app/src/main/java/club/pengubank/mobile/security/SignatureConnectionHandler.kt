package club.pengubank.mobile.security

import org.apache.commons.codec.binary.Base64
import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.signers.RSADigestSigner
import org.bouncycastle.crypto.util.PrivateKeyFactory
import org.bouncycastle.crypto.util.PublicKeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

class SignatureConnectionHandler(
    private val privateKey: PrivateKey,
    private val desktopPublicKey: PublicKey
) {

    fun signData(data: String): String {
        val dataBytes = data.encodeToByteArray()

        val signature = Signature.getInstance("SHA512withRSA").run {
            initSign(privateKey)
            update(dataBytes)
            sign()
        }

        return Base64.encodeBase64String(signature)
    }

    fun verifySignature(data: String, signature: String): Boolean {
        val dataBytes = data.encodeToByteArray()

        return Signature.getInstance("SHA512withRSA").run {
            initVerify(desktopPublicKey)
            update(dataBytes)
            verify(Base64.decodeBase64(signature))
        }
    }
}