package club.pengubank.mobile.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import club.pengubank.mobile.api.PenguBankApi
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.services.SetupService
import club.pengubank.mobile.services.TransactionService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.storage.UserDataSerializer
import club.pengubank.mobile.storage.UserDataService
import club.pengubank.mobile.storage.protos.UserData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.util.*
import javax.inject.Singleton

@KtorExperimentalAPI
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getSetupService(
        userDataService: UserDataService,
        store: StoreState,
        api: PenguBankApi
    ): SetupService =
        SetupService(userDataService, store, api)

    @Singleton
    @Provides
    fun getLoginService(userDataService: UserDataService, store: StoreState): LoginService =
        LoginService(userDataService, store)

    @Singleton
    @Provides
    fun getTransactionService(userDataService: UserDataService, store: StoreState): TransactionService =
        TransactionService(userDataService, store)

    @Singleton
    @Provides
    fun getStore(userDataService: UserDataService): StoreState = StoreState(userDataService)

    @Singleton
    @Provides
    fun getApi(@ApplicationContext context: Context, store: StoreState): PenguBankApi =
        PenguBankApi(context, store)

    @Singleton
    @Provides
    fun getUserDataService(userDataStore: DataStore<UserData>): UserDataService =
        UserDataService(userDataStore)

    @Singleton
    @Provides
    fun getUserDataStore(@ApplicationContext context: Context): DataStore<UserData> =
        context.createDataStore(
            fileName = "user_data.pb",
            serializer = UserDataSerializer
        )

}
