package com.challenge.weatherapp.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.challenge.weatherapp.networking.ApiClient
import com.challenge.weatherapp.utils.LAST_LOCATION
import com.challenge.weatherapp.utils.PREFERENCES
import com.challenge.weatherapp.viewModel.DashboardViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class Dashboard : ComponentActivity() {

    @Inject
    lateinit var apiClient: ApiClient

    private val viewModel: DashboardViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.compositeDisposable = compositeDisposable

        setContent { DashboardLayout(viewModel) }

        val lastlocation = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(LAST_LOCATION, null)

        if(lastlocation != null){

            viewModel.getWeather(lastlocation)

        }else {

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
                getLocation()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getLocation()
            }

            else -> {
                // No location access granted.
            }
        }


    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation.addOnSuccessListener {location : Location? ->
                location?.let {
                    val alt = it.altitude
                    val lon = it.longitude
                    viewModel.setLatLong(alt.toString(), lon.toString())
                }
            }
    }


}