package club.pengubank.mobile.data

import com.google.gson.annotations.SerializedName

data class Key(
    @SerializedName("secretKey") val id: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Key

        if (!id.contentEquals(other.id)) return false

        return true
    }

    override fun hashCode(): Int {
        return id.contentHashCode()
    }

    fun getSecretKey() : ByteArray {
        return id
    }
}
