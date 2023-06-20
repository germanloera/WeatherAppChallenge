package com.challenge.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import com.challenge.weatherapp.BuildConfig
import com.challenge.weatherapp.model.WeatherDataResponse
import com.challenge.weatherapp.networking.ApiClient
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


    val _icon = MutableStateFlow<String>("")
    val icon: StateFlow<String> = _icon

    val _temp = MutableStateFlow<String>("")
    val temp: StateFlow<String> = _temp

    private fun onIconChanged(icon: String) {
        _icon.value = "${icon}4x.png"
    }


    fun getWeather() {
        val params = mapOf("lat" to "18.50", "lon" to "-88.30", "appid" to BuildConfig.APP_TOKEN)

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


}