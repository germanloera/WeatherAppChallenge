package com.challenge.weatherchallenge.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.view.View
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
    //https://proandroiddev.com/two-way-data-binding-in-jetpack-compose-1be55c402ec6

    lateinit var binding: DashboardActivityBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val temp = ObservableField("")
    val locationTxt = ObservableField("")


    /// val icon = ObservableField("")
    // val temp = ObservableField("")

    /*
        val _weatherState = MutableStateFlow(WeatherDataResponse())
        val weatherResponse: StateFlow<WeatherDataResponse> = _weatherState.asStateFlow()

        lateinit var compositeDisposable: CompositeDisposable

        val _cityTextFieldState = MutableStateFlow("")
        val cityTextFieldValue: StateFlow<String> = _cityTextFieldState.asStateFlow()

        fun onChangeCityText(city: String) {
            _cityTextFieldState.value = city
        }


        val _errorStateCityTextField = MutableStateFlow(false)
        val errorValueCityTextField: StateFlow<Boolean> = _errorStateCityTextField.asStateFlow()

        fun onErrorStateTextFieldChange(value: Boolean) {
            _errorStateCityTextField.value = value
        }


        val _locationListState = MutableStateFlow(listOf<Location>())
        val locationList: StateFlow<List<Location>> = _locationListState.asStateFlow()

        fun setLocationList(locationResponse: List<Location>) {
            _locationListState.value = locationResponse
    */

    lateinit var compositeDisposable: CompositeDisposable

    val params = mutableMapOf<String, String>()
    private var lat = "18.50"
    private var lon = "-88.30"


    private fun setLatLong(lat: String, lon: String) {
        params.clear()
        this.lat = lat
        this.lon = lon
        params["lat"] = lat
        params["lon"] = lon

        getWeather()
    }

    fun getLastLocationWeather(location: String) {
        params.clear()
        params["q"] = location
        getWeather(location)
    }

    private fun getWeather(location: String? = null) {
        params["appid"] = BuildConfig.APP_TOKEN

        val disposable = api.getWeather(params).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { result ->

                    result.main?.let {
                        temp.set(it.F())
                    }


                    result.weather?.let {
                        val iconUrl = "https://openweathermap.org/img/wn/${it.first().icon}@4x.png"
                        Glide.with(app).load(iconUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.iconIv);
                    }

                    binding.location.text =    if(location.isNullOrBlank()) {


                         result.city()
                    }else{
                        location
                    }

                }, {
                    it.printStackTrace()
                }
            )

        compositeDisposable.add(disposable)

    }

    fun searchCity(view: View? = null) {
        params.clear()
        params["limit"] = "5"
        params["q"] = binding.locationTxt.text.toString()
        params["appid"] = BuildConfig.APP_TOKEN

        val disposable = api.getLocation(LOCATION_URL, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                val adapter = Adapter(it, app) { location -> loadCityWeather(location) }
                binding.rvLocations.adapter = adapter
                adapter.notifyDataSetChanged()


            }, {
                it.printStackTrace()
            })

        compositeDisposable.add(disposable)

    }


    private fun loadCityWeather(location: Location) {
        val locationName = location.locationName()
        params.clear()
        app.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit()
            .putString(LAST_LOCATION, locationName).apply()

        params["q"] = locationName.replace(", ", ",", false)
        getWeather(locationName)

    }


    @SuppressLint("MissingPermission")
    fun getLocation(view: View? = null) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)

        fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            location?.let {
                val alt = it.altitude
                val lon = it.longitude
                setLatLong(alt.toString(), lon.toString())
            }
        }
    }

}