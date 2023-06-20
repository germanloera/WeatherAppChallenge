package com.challenge.weatherapp.di

import android.app.Application
import android.content.Context
import com.challenge.weatherapp.BuildConfig
import com.challenge.weatherapp.networking.ApiClient
import com.challenge.weatherapp.utils.API_TOKEN_HEADER
import com.challenge.weatherapp.utils.TIMEOUT_SECONDS
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule() {


  /*  @Provides
    @Singleton
    fun getApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun getContext(): Context {
        return application
    }*/

    @Provides
    @Singleton
    fun gsonConverterFactory() : GsonConverterFactory{
        return GsonConverterFactory.create() }

    @Provides
    @Singleton
    fun getClient(): OkHttpClient {
        val client =
            OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

       val interceptor = LoggingInterceptor.Builder().setLevel(Level.BODY).build()
       client.addInterceptor(interceptor)

       return client.build()
   }


  @Provides
   @Singleton
   fun getRetrofit(client: OkHttpClient, factory: GsonConverterFactory): Retrofit {
     return Retrofit.Builder()
         .baseUrl(BuildConfig.BASE_URL)
         .addConverterFactory(factory)
         .client(client)
         .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
         .build()

  }

   @Provides
   @Singleton
   fun getApiClient(retrofit: Retrofit): ApiClient{
       return retrofit.create(ApiClient::class.java)
   }


}