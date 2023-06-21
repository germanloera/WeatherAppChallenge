package com.challenge.weatherapp.model

import com.challenge.weatherapp.utils.Constants

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
) {
    fun F() = "${((temp - 273.15) * 9 / 5 + 32).toInt()}°"
}

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)

data class WeatherDataResponse(
    val coord: Coord? = null,
    val weather: List<Weather>? = null,
    val base: String? = null,
    val main: Main? = null,
    val visibility: Int? = null,
    val wind: Wind? = null,
    val clouds: Clouds? = null,
    val dt: Long? = null,
    val sys: Sys? = null,
    val timezone: Int? = null,
    val id: Int? = null,
    val name: String? = "",
    val cod: Int? = null,
    var label : String?=null
) {
    fun city(): String {
        var text = ""

        name?.let {
            if (it.isNotBlank()) {
                text = it
            }
        }
        sys?.country?.let {
            if (it.isNotBlank()) {
                text = "$text, $it"
            }
        }

        return text
    }


}

data class Location(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    var state: String? = null
){

    fun locationName(): String {
        return if(country.equals("us", true)){
            state = Constants.states[this.state]
            "$name, $state, $country"
        }else{
            "$name, $country"
        }
    }


}
