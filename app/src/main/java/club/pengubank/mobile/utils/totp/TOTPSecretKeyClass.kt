package club.pengubank.mobile.utils.totp

import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Base64

/**
 * OTP authenticator key.
 */
data class TOTPSecretKey(val value: ByteArray) {
    enum class KeyRepresentation { BASE32, BASE64 }

    fun to(representation: KeyRepresentation): String {
        return when (representation) {
            KeyRepresentation.BASE32 -> Base32().encodeToString(value)
            KeyRepresentation.BASE64 -> Base64().encodeToString(value)
        }
    }

    override fun toString(): String = to(KeyRepresentation.BASE64)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as TOTPSecretKey
        if (!value.contentEquals(other.value)) return false
        return true
    }

    override fun hashCode(): Int = value.contentHashCode()

    companion object {
        fun from(representation: KeyRepresentation = KeyRepresentation.BASE64, value: String): TOTPSecretKey {
            return when (representation) {
                KeyRepresentation.BASE32 -> TOTPSecretKey(Base32().decode(value))
                KeyRepresentation.BASE64 -> TOTPSecretKey(Base64().decode(value))
            }
        }
    }
}