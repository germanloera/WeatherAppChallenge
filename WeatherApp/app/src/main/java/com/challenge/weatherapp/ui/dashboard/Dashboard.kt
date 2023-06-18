package com.challenge.weatherapp.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.challenge.weatherapp.BuildConfig
import com.challenge.weatherapp.R
import com.challenge.weatherapp.databinding.ActivityDashboardBinding
import com.challenge.weatherapp.di.BaseApp
import com.challenge.weatherapp.networking.ApiClient
import dagger.android.AndroidInjector
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Dashboard : AppCompatActivity() {

    @Inject
    lateinit var apiClient: ApiClient

lateinit var binding : ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(LayoutInflater.from(this))
        val app = application as BaseApp
        app.component.inject(this)
        setContentView(binding.root)

        //lat=44.34&lon=10.99&appid=
        val params = mapOf("lat" to "44.34", "lon" to "10.99", "appid" to BuildConfig.APP_TOKEN)




        apiClient.getWeather(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            { result ->

            },{
                it.printStackTrace()
            }
        )




    }


}