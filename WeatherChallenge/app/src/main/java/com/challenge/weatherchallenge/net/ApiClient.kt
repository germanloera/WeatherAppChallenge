package com.challenge.weatherchallenge.net


import com.challenge.weatherchallenge.model.Location
import com.challenge.weatherchallenge.model.WeatherDataResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface ApiClient {

    @GET(".")
    fun getWeather(@QueryMap params: Map<String, String>): Observable<WeatherDataResponse>

    @GET
    fun getLocation(
        @Url url: String,
        @QueryMap params: Map<String, String>
    ): Observable<ArrayList<Location>>


}