package com.challenge.weatherchallenge.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class for the base application.
 * It extends the Android Application class to represent the base application.
 * The class is annotated with @HiltAndroidApp to enable Hilt dependency injection framework.
 * This annotation triggers the code generation necessary for Hilt to work.
 */
@HiltAndroidApp
class BaseApp : Application()
