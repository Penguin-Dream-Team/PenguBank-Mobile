package club.pengubank.mobile.di

import club.pengubank.mobile.api.PenguBankApi
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.StoreState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getLoginService(store: StoreState, api: PenguBankApi): LoginService = LoginService(store, api)

    @Singleton
    @Provides
    fun getStore(): StoreState = StoreState()

    @Singleton
    @Provides
    fun getApi(): PenguBankApi = PenguBankApi()
}