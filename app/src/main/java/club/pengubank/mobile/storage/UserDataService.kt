package club.pengubank.mobile.storage

import android.util.Log
import androidx.datastore.core.DataStore
import club.pengubank.mobile.storage.protos.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException

class UserDataService(private val userDataStore: DataStore<UserData>) {

    private val userPreferencesFlow: Flow<UserData> = userDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(UserData.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun getUserData(): UserData =
        userPreferencesFlow.firstOrNull() ?: UserData.getDefaultInstance()

    suspend fun storeUserData(
        email: String? = null,
        passcode: String? = null,
        totpKey: String? = null
    ) {
        userDataStore.updateData {
            val userDataBuilder = it.toBuilder()

            if (email.isNullOrBlank().not())
                userDataBuilder.email = email
            if (passcode.isNullOrBlank().not())
                userDataBuilder.passcode = passcode
            if (totpKey.isNullOrBlank().not())
                userDataBuilder.totpKey = totpKey

            userDataBuilder.build()
        }
    }

    companion object {
        private const val TAG = "UserDataService"
    }
}
