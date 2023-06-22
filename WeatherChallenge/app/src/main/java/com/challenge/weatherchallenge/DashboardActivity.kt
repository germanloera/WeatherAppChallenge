package com.challenge.weatherchallenge


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.weatherchallenge.databinding.DashboardActivityBinding
import com.challenge.weatherchallenge.utils.LAST_LOCATION
import com.challenge.weatherchallenge.utils.PREFERENCES
import com.challenge.weatherchallenge.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    private val viewModel: DashboardViewModel by viewModels()
    lateinit var binding: DashboardActivityBinding
    private val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardActivityBinding.inflate(LayoutInflater.from(this))

        binding.rvLocations.layoutManager = LinearLayoutManager(this)
        binding.viewModel = viewModel
        viewModel.binding = binding
        viewModel.compositeDisposable = this.compositeDisposable

        setContentView(binding.root)

        val lastlocation = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            .getString(LAST_LOCATION, null)

        if (lastlocation != null) {

            viewModel.getLastLocationWeather(lastlocation)

        } else {
            requestLocationPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }


    @SuppressLint("MissingPermission")
    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                viewModel.getLocation()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                viewModel.getLocation()
            }

            else -> {
                // No location access granted.
            }
        }


    }


}