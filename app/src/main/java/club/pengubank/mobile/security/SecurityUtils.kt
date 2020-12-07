package club.pengubank.mobile.security

import android.icu.util.GregorianCalendar
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.jcajce.JcaPEMWriter
import java.io.StringReader
import java.io.StringWriter
import java.math.BigInteger
import java.security.*
import java.security.cert.Certificate
import java.util.*
import javax.security.auth.x500.X500Principal

object SecurityUtils {

    private var keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
    private var KEY_ALIAS = "PenguBank-key"

    fun init() {
        keyStore.load(null)
        if (!keyStore.containsAlias(KEY_ALIAS)) { generateKeyPair() }
    }

    fun sign(data: String) {
        return Signature.getInstance("SHA512withRSA").run{
            initSign(getPrivateKey())
            update(data.toByteArray())
            sign()
        }
    }

    fun validate(data: String, signature: ByteArray, publicKey: PublicKey) {
        return Signature.getInstance("SHA512withRSA").run{
            initVerify(publicKey)
            update(data.toByteArray())
            verify(signature)
        }
    }

    fun getPublicKey(): PublicKey = keyStore.getCertificate(KEY_ALIAS).publicKey as PublicKey

    fun parsePublicKey(publicKeyPEM: String): PublicKey {
        val textReader = StringReader(publicKeyPEM)
        val pemParser = PEMParser(textReader)
        val converter = JcaPEMKeyConverter()
        return converter.getPublicKey(
            SubjectPublicKeyInfo.getInstance(
            pemParser.readObject()
        ))
    }

    fun writePublicKey(publicKey: PublicKey): String {
        val stringWriter = StringWriter()
        val pemWriter = JcaPEMWriter(stringWriter)
        pemWriter.writeObject(publicKey)
        pemWriter.flush()
        return stringWriter.toString()
    }

    private fun getPrivateKey(): PrivateKey = keyStore.getKey(KEY_ALIAS, null) as PrivateKey

    private fun generateKeyPair() {
        val startDate = GregorianCalendar()
        startDate.add(Calendar.DAY_OF_MONTH, -1)

        val endDate = GregorianCalendar()
        endDate.add(Calendar.YEAR, 2)

        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")

        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).run {
                setCertificateSerialNumber(BigInteger.valueOf(777))
                setCertificateSubject(X500Principal("CN=$KEY_ALIAS"))
                setDigests(KeyProperties.DIGEST_SHA512)
                setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                setCertificateNotBefore(startDate.time)
                setCertificateNotAfter(endDate.time)
                build()
        }

        keyPairGenerator.initialize(parameterSpec)

        keyPairGenerator.genKeyPair()
    }
}