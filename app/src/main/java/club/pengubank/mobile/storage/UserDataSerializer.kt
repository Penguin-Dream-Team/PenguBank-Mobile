package club.pengubank.mobile.storage

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import club.pengubank.mobile.bluetooth.models.JSONObject
import club.pengubank.mobile.bluetooth.models.toObject
import club.pengubank.mobile.security.SecurityUtils
import club.pengubank.mobile.storage.protos.UserData
import com.google.gson.Gson
import com.google.protobuf.InvalidProtocolBufferException
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.io.OutputStream

object UserDataSerializer : Serializer<UserData> {
    override val defaultValue: UserData = UserData.getDefaultInstance()

    override fun readFrom(input: InputStream): UserData =
        try {
            SecurityUtils.init()

            val secretKey = SecurityUtils.getSecretKey()
            val publicKey = SecurityUtils.getPublicKey()

            val dataInputStream = DataInputStream(input)

            val securedUserData = dataInputStream.readUTF().toObject<SecureUserData>()

            val jsonData =
                SecurityUtils.decipher(
                    secretKey,
                    securedUserData.data,
                    securedUserData.iv,
                    securedUserData.tagLen
                )
            val valid =
                SecurityUtils.verifySignature(jsonData, securedUserData.signature, publicKey)

            if (!valid)
                throw CorruptionException("Datastore has been tampered with")

            Gson().fromJson(jsonData, UserData::class.java)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }

    override fun writeTo(t: UserData, output: OutputStream) {
        val secretKey = SecurityUtils.getSecretKey()

        val jsonData = Gson().toJson(t)
        val (cipheredJsonData, parameterSpec) = SecurityUtils.cipher(secretKey, jsonData)
        val signature = SecurityUtils.signData(jsonData)

        val securedUserData = SecureUserData(
            cipheredJsonData,
            signature,
            parameterSpec.iv,
            parameterSpec.tLen
        )

        val dataOutputStream = DataOutputStream(output)
        dataOutputStream.writeUTF(securedUserData.toJSON())
        dataOutputStream.flush()
    }
}

data class SecureUserData(
    val data: String,
    val signature: String,
    val iv: ByteArray,
    val tagLen: Int
) : JSONObject
