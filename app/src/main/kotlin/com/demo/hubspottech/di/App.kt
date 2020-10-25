package com.demo.hubspottech.di

import com.demo.hubspottech.BuildConfig
import com.demo.hubspottech.BuildConfig.DEBUG
import com.demo.hubspottech.di.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import timber.log.Timber.plant

open class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        if (DEBUG) plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent
            .builder()
            .application(this)
            .build()

    open fun getBaseUrl() = BuildConfig.BASE_URL
}