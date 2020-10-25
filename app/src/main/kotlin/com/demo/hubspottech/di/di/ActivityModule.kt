package com.demo.hubspottech.di.di

import com.demo.hubspottech.di.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributesQRActivity(): MainActivity
}