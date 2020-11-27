package club.pengubank.mobile.di

import club.pengubank.mobile.api.PenguBankApi
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.StoreState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
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
    fun getApi(store: StoreState): PenguBankApi = PenguBankApi(store)
}