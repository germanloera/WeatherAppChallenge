package com.challenge.weatherapp.di

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class BaseApp : Application(), HasAndroidInjector {

    lateinit var component: AppComponent

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        setupGraph()
    }

    private fun setupGraph() {
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()

    }

    override fun androidInjector(): AndroidInjector<Any> {
        return injector
    }
}