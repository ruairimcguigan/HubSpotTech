package com.demo.hubspottech.di.repository

import com.demo.hubspottech.di.api.HubSpotService
import com.demo.hubspottech.di.rx.DefaultSchedulerProvider
import com.demo.hubspottech.di.rx.RxDisposable
import dagger.Module
import dagger.Provides

@Module
class RepoModule {

    @Provides
    fun provideRxScheduler() = DefaultSchedulerProvider()

    @Provides
    fun provideDisposable() = RxDisposable()

    @Provides
    fun providesRepository(
        disposable: RxDisposable,
        defaultSchedulerProvider: DefaultSchedulerProvider,
        service: HubSpotService,
        jsonProcessor: JsonProcessor
    ): Repository = DefaultRepository(
        service = service,
        disposable = disposable,
        schedulerProvider = defaultSchedulerProvider,
        jsonProcessor = jsonProcessor
    )

    @Provides
    fun provideJsonProcessor(): JsonProcessor = DefaultJsonProcessor()
}