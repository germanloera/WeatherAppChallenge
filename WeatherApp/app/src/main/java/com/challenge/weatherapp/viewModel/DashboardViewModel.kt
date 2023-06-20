package com.challenge.weatherapp.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.challenge.weatherapp.BuildConfig
import com.challenge.weatherapp.networking.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val api: ApiClient) : ViewModel() {
    //https://proandroiddev.com/two-way-data-binding-in-jetpack-compose-1be55c402ec6

    val icon = ObservableField("")
    val temp = ObservableField("")





    fun getWeather(){
        val params = mapOf("lat" to "44.34", "lon" to "10.99", "appid" to BuildConfig.APP_TOKEN)

        api.getWeather(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            { result ->

            },{
                it.printStackTrace()
            }
        )

    }


}