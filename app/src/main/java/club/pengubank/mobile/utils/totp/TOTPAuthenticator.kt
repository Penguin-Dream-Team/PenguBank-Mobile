package club.pengubank.mobile.utils.totp

import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.time.Instant
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/**
 * OTP authenticator library interface.
 */
class TOTPAuthenticator() {
    private val config = TOTPConfig()

    fun calculateValidationCode(key: ByteArray): Int = calculateCode(key, 0)

    fun calculateLastValidationCode(key: ByteArray): Int = calculateCode(key, getTimeWindowFromTime())

    fun getTimeWindowFromTime(time: Instant = Instant.now()) = time.toEpochMilli() / config.timeStepSize.toMillis()

    /**
     * Calculates the verification code of the provided key at the specified
     * instant of time using the algorithm specified in RFC 6238.
     *
     * @param key the secret key in binary format.
     * @param timestamp the instant of time.
     * @return the validation code for the provided key at the specified instant of time.
     */
    private fun calculateCode(key: ByteArray, timestamp: Long): Int {
        // Converting the instant of time from the long representation to a  big-endian array of bytes (RFC4226, 5.2. Description).
        val bigEndianTimestamp = ByteArray(8)
        var value = timestamp
        var byte = 8
        while (byte-- > 0) {
            bigEndianTimestamp[byte] = value.toByte()
            value = value ushr 8
        }

        // Building the secret key specification for the HmacSHA1 algorithm.
        val signKey = SecretKeySpec(key, HMAC_HASH_FUNCTION)

        try {
            // Getting an HmacSHA1 algorithm implementation from the JCE.
            val mac = Mac.getInstance(HMAC_HASH_FUNCTION)
            mac.init(signKey)

            // Processing the instant of time and getting the encrypted data.
            val hash = mac.doFinal(bigEndianTimestamp)

            // Building the validation code performing dynamic truncation (RFC4226, 5.3. Generating an HOTP value)
            val offset = hash[hash.size - 1] and 0xF

            // We are using a long because Java hasn't got an unsigned integer type and we need 32 unsigned bits).
            var truncatedHash: Long = 0

            for (i in 0..3) {
                truncatedHash = truncatedHash shl 8

                // Java bytes are signed but we need an unsigned integer: cleaning off all but the LSB.
                truncatedHash = truncatedHash or (hash[offset + i].toInt() and 0xFF).toLong()
            }

            // Clean bits higher than the 32nd (inclusive) and calculate the module with the maximum validation code value.
            truncatedHash = truncatedHash and 0x7FFFFFFF
            truncatedHash %= config.keyModulus

            return truncatedHash.toInt()
        } catch (e: NoSuchAlgorithmException) {
            throw TOTPException("The operation cannot be performed now.", e)
        } catch (e: InvalidKeyException) {
            throw TOTPException("The operation cannot be performed now.", e)
        }
    }

    companion object {
        private const val HMAC_HASH_FUNCTION = "HmacSHA1"
    }
}
