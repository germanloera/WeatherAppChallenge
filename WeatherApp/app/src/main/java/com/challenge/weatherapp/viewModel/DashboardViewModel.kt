package com.challenge.weatherapp.viewModel

import android.provider.SyncStateContract.Constants
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.ViewModel
import com.challenge.weatherapp.BuildConfig
import com.challenge.weatherapp.model.WeatherDataResponse
import com.challenge.weatherapp.networking.ApiClient
import com.challenge.weatherapp.utils.LOCATION_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val api: ApiClient) : ViewModel() {
    //https://proandroiddev.com/two-way-data-binding-in-jetpack-compose-1be55c402ec6

    /// val icon = ObservableField("")
    // val temp = ObservableField("")

    val _weatherState = MutableStateFlow(WeatherDataResponse())
    val weatherResponse: StateFlow<WeatherDataResponse> = _weatherState.asStateFlow()

    lateinit var compositeDisposable: CompositeDisposable

    val _cityTextFieldState = MutableStateFlow("")
    val cityTextFieldValue : StateFlow<String> = _cityTextFieldState.asStateFlow()

    fun onChangeCityText(city: String){
        _cityTextFieldState.value = city
    }


    val _errorStateCityTextField = MutableStateFlow(false)
    val errorValueCityTextField : StateFlow<Boolean> = _errorStateCityTextField.asStateFlow()

    fun onErrorStateTextFieldChange(value: Boolean){
        _errorStateCityTextField.value = value
    }


   /* val _icon = MutableStateFlow<String>("")
    val icon: StateFlow<String> = _icon

    val _temp = MutableStateFlow<String>("")
    val temp: StateFlow<String> = _temp

    private fun onIconChanged(icon: String) {
        _icon.value = "${icon}4x.png"
    }
*/
    val params = mutableMapOf<String, String>()
    private var lat = "18.50"
    private var lon = "-88.30"


    fun setLatLong(lat: String, lon:String){
        params.clear()
        this.lat = lat
        this.lon = lon
        params["lat"] = lat
        params["lon"] = lon

        getWeather()
    }




    private fun getWeather() {
        params["appid"] = BuildConfig.APP_TOKEN

        val disposable = api.getWeather(params).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { result ->
                    _weatherState.value = result
                }, {
                    it.printStackTrace()
                }
            )

        compositeDisposable.add(disposable)

    }

    fun searchCity(){
        params.clear()
        params["limit"] = "5"
        params["q"] = cityTextFieldValue.value
        params["appid"] = BuildConfig.APP_TOKEN


        val disposable =  api.getLocation(LOCATION_URL,  params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},{})

        compositeDisposable.add(disposable)

    }





}