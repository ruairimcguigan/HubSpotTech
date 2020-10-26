package com.demo.hubspottech.di.repository

import com.demo.hubspottech.di.api.HubSpotService
import com.demo.hubspottech.di.main.BookingProcessor
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
        bookingProcessor: BookingProcessor
    ): Repository = DefaultRepository(
        service = service,
        disposable = disposable,
        schedulerProvider = defaultSchedulerProvider,
        bookingProcessor = bookingProcessor
    )

    @Provides
    fun provideBookingProcessor(): BookingProcessor = BookingProcessor()
}