package com.challenge.weatherapp.networking


import com.challenge.weatherapp.model.WeatherDataResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiClient {

    @GET(".")
    fun getWeather(@QueryMap params: Map<String, String>) : Observable<WeatherDataResponse>

}