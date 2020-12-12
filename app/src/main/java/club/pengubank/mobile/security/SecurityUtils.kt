package club.pengubank.mobile.security

import android.icu.util.GregorianCalendar
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import org.apache.commons.codec.binary.Base64
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
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.security.auth.x500.X500Principal

object SecurityUtils {

    private var keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
    private var KEY_ALIAS = "PenguBank-key"
    private var SECRET_KEY_ALIAS = "PenguBank-secretkey"

    fun init() {
        keyStore.load(null)
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            generateKeyPair()
        }
        if (!keyStore.containsAlias(SECRET_KEY_ALIAS)) {
            generateSecretKey()
        }
    }

    fun signData(data: String): String {
        val dataBytes = data.encodeToByteArray()

        val signature = Signature.getInstance("SHA512withRSA").run {
            initSign(getPrivateKey())
            update(dataBytes)
            sign()
        }

        return Base64.encodeBase64String(signature)
    }

    fun verifySignature(data: String, signature: String, publicKey: PublicKey): Boolean {
        val dataBytes = data.encodeToByteArray()

        return Signature.getInstance("SHA512withRSA").run {
            initVerify(publicKey)
            update(dataBytes)
            verify(Base64.decodeBase64(signature))
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
            )
        )
    }

    fun writePublicKey(publicKey: PublicKey): String {
        val stringWriter = StringWriter()
        val pemWriter = JcaPEMWriter(stringWriter)
        pemWriter.writeObject(publicKey)
        pemWriter.flush()
        return stringWriter.toString()
    }

    private fun getPrivateKey(): PrivateKey = keyStore.getKey(KEY_ALIAS, null) as PrivateKey

    fun getSecretKey(): SecretKey = keyStore.getKey(SECRET_KEY_ALIAS, null) as SecretKey

    private fun generateKeyPair() {
        val startDate = GregorianCalendar()
        startDate.add(Calendar.DAY_OF_MONTH, -1)

        val endDate = GregorianCalendar()
        endDate.add(Calendar.YEAR, 2)

        val keyPairGenerator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")

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

    private fun generateSecretKey() {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val parameterSpec = KeyGenParameterSpec.Builder(
            SECRET_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).run {
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            setRandomizedEncryptionRequired(true)
            build()
        }

        keyGenerator.init(parameterSpec)

        keyGenerator.generateKey()
    }

    fun cipher(secretKey: SecretKey, data: String): Pair<String, GCMParameterSpec> {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val parameterSpec = cipher.parameters.getParameterSpec(GCMParameterSpec::class.java)
        return Pair(
            Base64.encodeBase64String(cipher.doFinal(data.encodeToByteArray())),
            parameterSpec
        )
    }

    fun decipher(secretKey: SecretKey, data: String, iv: ByteArray, tagLen: Int): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(tagLen, iv)
        )
        return String(cipher.doFinal(Base64.decodeBase64(data)))
    }
}