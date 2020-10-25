package com.demo.hubspottech.di.di

import android.app.Application
import com.demo.hubspottech.di.App
import com.demo.hubspottech.di.api.ApiModule
import com.demo.hubspottech.di.repository.RepoModule
import com.demo.hubspottech.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        ApiModule::class,
        RepoModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}