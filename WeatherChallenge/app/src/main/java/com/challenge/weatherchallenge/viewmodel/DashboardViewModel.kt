package com.challenge.weatherchallenge.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.challenge.weatherchallenge.Adapter
import com.challenge.weatherchallenge.BuildConfig
import com.challenge.weatherchallenge.databinding.DashboardActivityBinding
import com.challenge.weatherchallenge.di.BaseApp
import com.challenge.weatherchallenge.model.Location
import com.challenge.weatherchallenge.net.ApiClient
import com.challenge.weatherchallenge.utils.LAST_LOCATION
import com.challenge.weatherchallenge.utils.LOCATION_URL
import com.challenge.weatherchallenge.utils.PREFERENCES
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val api: ApiClient, val app: BaseApp) : ViewModel() {

    lateinit var binding: DashboardActivityBinding
    lateinit var compositeDisposable: CompositeDisposable

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val params = mutableMapOf<String, String>()
    private var lat = "18.50"
    private var lon = "-88.30"

    val temp = ObservableField("")
    val locationTxt = ObservableField("")



    /**
     * Sets the latitude and longitude values for weather data retrieval.
     * Clears any existing parameters, updates the local variables,
     * and adds the latitude and longitude to the parameters map.
     * Calls the `getWeather()` function to retrieve weather information using the updated latitude and longitude.
     */
    private fun setLatLong(lat: String, lon: String) {
        params.clear() // Clear any existing parameters

        // Update local variables with the provided latitude and longitude values
        this.lat = lat
        this.lon = lon

        // Add latitude and longitude to the parameters map
        params["lat"] = lat
        params["lon"] = lon

        getWeather() // Call the `getWeather()` function to retrieve weather data
    }

    /**
     * Retrieves the weather information for the last known location based on the provided location string.
     * Clears any existing parameters, sets the "q" parameter with the provided location value,
     * and calls the `getWeather(location)` function to retrieve the weather data.
     */
    fun getLastLocationWeather(location: String) {
        params.clear() // Clear any existing parameters

        // Set the "q" parameter with the provided location value
        params["q"] = location

        getWeather(location) // Call the `getWeather(location)` function to retrieve weather data for the specified location
    }


    /**
     * Retrieves weather information based on the provided location or the last known location.
     * Sets the "appid" parameter with the application token from BuildConfig.
     * Makes an API call to get the weather data and updates the UI with the received data.
     */
    private fun getWeather(location: String? = null) {
        params["appid"] = BuildConfig.APP_TOKEN // Set the "appid" parameter with the application token

        // Make an API call to get the weather data using the `api` object
        val disposable = api.getWeather(params)
            .subscribeOn(Schedulers.io()) // Perform the API call on the background IO thread
            .observeOn(AndroidSchedulers.mainThread()) // Observe the result on the main UI thread
            .subscribe(
                { result ->
                    result.main?.let {
                        temp.set(it.F()) // Update the temperature value using the received data
                    }

                    result.weather?.let {
                        val iconUrl = "https://openweathermap.org/img/wn/${it.first().icon}@4x.png"
                        // Load the weather icon using Glide and update the ImageView in the UI
                        Glide.with(app).load(iconUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.iconIv)
                    }

                    // Update the location text based on the provided location or the received city name
                    binding.location.text = if (location.isNullOrBlank()) {
                        result.city() // Use the received city name
                    } else {
                        location // Use the provided location
                    }
                },
                { error ->
                    error.printStackTrace() // Print the error stack trace in case of an API call failure
                    Toast.makeText(app, "Connection Error ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

        compositeDisposable.add(disposable) // Add the disposable to the compositeDisposable for cleanup
    }


    /**
     * Searches for cities based on the text entered in the location text field.
     * Clears any existing parameters, sets the "limit" parameter to 5, sets the "q"
     * parameter with the text entered, and sets the "appid" parameter with the
     * application token from BuildConfig.
     * Makes an API call to get the locations matching the search query and updates
     * the RecyclerView with the results.
     */
    fun searchCity(view: View? = null) {
        params.clear() // Clear any existing parameters

        params["limit"] = "5" // Set the "limit" parameter to 5

        // Set the "q" parameter with the text entered in the binding.locationTxt EditText
        params["q"] = binding.locationTxt.text.toString()

        params["appid"] = BuildConfig.APP_TOKEN // Set the "appid" parameter with the application token

        // Make an API call to get the locations matching the search query using the `api` object
        val disposable = api.getLocation(LOCATION_URL, params)
            .subscribeOn(Schedulers.io()) // Perform the API call on the background IO thread
            .observeOn(AndroidSchedulers.mainThread()) // Observe the result on the main UI thread
            .subscribe(
                { locations ->
                    val adapter = Adapter(locations, app) { location -> loadCityWeather(location) }
                    // Set the adapter for the RecyclerView and notify it of the data change
                    binding.rvLocations.adapter = adapter
                    adapter.notifyDataSetChanged()
                },
                { error ->
                    error.printStackTrace() // Print the error stack trace in case of an API call failure
                }
            )

        compositeDisposable.add(disposable) // Add the disposable to the compositeDisposable for cleanup
    }


    /**
     * Loads the weather information for a specific city location.
     * Retrieves the name of the location and stores it in shared preferences as the last location.
     * Clears any existing parameters, sets the "q" parameter with the formatted location name,
     * and calls the `getWeather(locationName)` function to retrieve the weather data.
     */
    private fun loadCityWeather(location: Location) {
        val locationName = location.locationName() // Get the name of the location

        params.clear() // Clear any existing parameters

        // Store the location name in shared preferences as the last location
        app.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit()
            .putString(LAST_LOCATION, locationName).apply()

        // Set the "q" parameter with the formatted location name (replace ", " with ",")
        params["q"] = locationName.replace(", ", ",", false)

        getWeather(locationName) // Call the `getWeather(locationName)` function to retrieve the weather data
    }


    /**
     * Retrieves the current device location.
     * Uses the FusedLocationProviderClient to get the last known location.
     * If a location is available, it extracts the altitude and longitude values.
     * Calls the `setLatLong` function to set the latitude and longitude values for weather retrieval.
     */
    @SuppressLint("MissingPermission")
    fun getLocation(view: View? = null) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)

        // Get the last known location using the FusedLocationProviderClient
        fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            location?.let {
                val alt = it.altitude // Extract the altitude value from the location
                val lon = it.longitude // Extract the longitude value from the location
                setLatLong(alt.toString(), lon.toString()) // Set the latitude and longitude values for weather retrieval
            }
        }
    }


}