package club.pengubank.mobile.storage

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import club.pengubank.mobile.storage.protos.UserData
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserDataSerializer : Serializer<UserData> {
    override val defaultValue: UserData = UserData.getDefaultInstance()

    override fun readFrom(input: InputStream): UserData =
        try {
            UserData.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }

    override fun writeTo(t: UserData, output: OutputStream) = t.writeTo(output)
}
