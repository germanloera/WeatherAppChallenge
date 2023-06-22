package com.challenge.weatherchallenge.net


import com.challenge.weatherchallenge.model.Location
import com.challenge.weatherchallenge.model.WeatherDataResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface ApiClient {

    /**
     * Retrofit API method used to retrieve weather data.
     * Performs a GET request to the base URL (".") with query parameters provided as a map.
     * Expects the response to be a WeatherDataResponse object wrapped in an Observable.
     */
    @GET(".")
    fun getWeather(@QueryMap params: Map<String, String>): Observable<WeatherDataResponse>

    /**
     * Retrofit API method used to retrieve location information.
     * Performs a GET request to the specified URL, appending query parameters from the provided map.
     * Expects the response to be an ArrayList of Location objects wrapped in an Observable.
     */
    @GET
    fun getLocation(
        @Url url: String,
        @QueryMap params: Map<String, String>
    ): Observable<ArrayList<Location>>


}