package com.demo.hubspottech.di.api

import android.app.Application
import com.demo.hubspottech.di.App
import com.demo.hubspottech.di.network.DefaultNetworkState
import com.demo.hubspottech.di.network.NetworkState
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(
        app: Application,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
            .baseUrl((app as App).getBaseUrl())
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    fun provideReposApi(retrofit: Retrofit): HubSpotApi =
        retrofit.create(HubSpotApi::class.java)

    @Provides
    fun provideReposService(repoApi: HubSpotApi) = HubSpotService(repoApi)

    @Provides
    fun provideNetworkStateCheck(app: Application): NetworkState = DefaultNetworkState(app)
}