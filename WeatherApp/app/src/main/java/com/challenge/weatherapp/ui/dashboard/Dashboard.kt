package com.challenge.weatherapp.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.challenge.weatherapp.BuildConfig
import com.challenge.weatherapp.R
import com.challenge.weatherapp.databinding.ActivityDashboardBinding
import com.challenge.weatherapp.di.BaseApp
import com.challenge.weatherapp.networking.ApiClient
import com.challenge.weatherapp.viewModel.DashboardViewModel
import dagger.android.AndroidInjector
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class Dashboard : ComponentActivity() {

    @Inject
    lateinit var apiClient: ApiClient

    private val viewModel : DashboardViewModel by viewModels()

lateinit var binding : ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
       // binding = ActivityDashboardBinding.inflate(LayoutInflater.from(this))
       // val app = application as BaseApp
        //app.component.inject(this)

viewModel.getWeather()
        setContent { DashboardLayout(viewModel) }


        //lat=44.34&lon=10.99&appid=








    }


}