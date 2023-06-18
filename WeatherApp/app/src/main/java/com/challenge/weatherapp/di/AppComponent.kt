package com.challenge.weatherapp.di

import com.challenge.weatherapp.ui.dashboard.Dashboard
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ AppModule::class, AndroidModule::class])
public interface AppComponent {

    fun inject(activity: Dashboard)
}