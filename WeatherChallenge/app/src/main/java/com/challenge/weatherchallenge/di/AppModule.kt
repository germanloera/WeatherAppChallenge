package com.challenge.weatherchallenge.di

import android.content.Context
import com.challenge.weatherchallenge.BuildConfig
import com.challenge.weatherchallenge.net.ApiClient
import com.challenge.weatherchallenge.utils.TIMEOUT_SECONDS
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Module class that provides dependencies for the application.
 * It includes methods annotated with @Provides to provide instances of various dependencies.
 * These dependencies include GsonConverterFactory, OkHttpClient, Retrofit, ApiClient,
 * and the application's BaseApp instance.
 * The dependencies are configured as singletons, ensuring that only one instance is created and reused.
 * The provided instances are used for network operations, such as creating a Retrofit client
 * and configuring the API interface.
 */
@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun gsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun getClient(): OkHttpClient {
        // Create an OkHttpClient with timeout configurations
        // and add a logging interceptor for debugging purposes
        // The OkHttpClient instance is configured as a singleton
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(LoggingInterceptor.Builder().setLevel(Level.BODY).build())
            .build()
    }

    @Provides
    @Singleton
    fun getRetrofit(client: OkHttpClient, factory: GsonConverterFactory): Retrofit {
        // Create a Retrofit instance with the provided OkHttpClient, GsonConverterFactory,
        // and other configurations such as the base URL and RxJava call adapter factory
        // The Retrofit instance is configured as a singleton
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(factory)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getApiClient(retrofit: Retrofit): ApiClient {
        // Create an instance of the ApiClient interface using the Retrofit instance
        // The ApiClient instance is configured as a singleton
        return retrofit.create(ApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApp {
        // Provide the application's BaseApp instance
        // The BaseApp instance is configured as a singleton
        return app as BaseApp
    }
}
