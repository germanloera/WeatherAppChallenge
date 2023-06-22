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

/**
 * An activity that serves as the dashboard screen of the application.
 * It is annotated with @AndroidEntryPoint to enable Hilt dependency injection.
 * The activity extends ComponentActivity provided by AndroidX.
 * It includes a view model instance of DashboardViewModel injected by Hilt using the by viewModels() delegate.
 * The activity uses Data Binding for UI layout inflation.
 * It manages a CompositeDisposable to handle RxJava disposables.
 * The activity overrides the onCreate() method to set up the activity's UI and initialize the view model.
 * It also handles the permission request for location access using ActivityResultContracts.
 * The last known location is retrieved from SharedPreferences and used to fetch weather data.
 * If no last known location is available, the activity requests location permission from the user.
 * The activity handles the result of the permission request and initiates location retrieval.
 * Location retrieval is performed using the getLocation() method of the view model.
 * The activity overrides the onDestroy() method to dispose of the CompositeDisposable.
 */
@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    private val viewModel: DashboardViewModel by viewModels()
    lateinit var binding: DashboardActivityBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardActivityBinding.inflate(LayoutInflater.from(this))

        // Set up RecyclerView and bind the view model
        binding.rvLocations.layoutManager = LinearLayoutManager(this)
        binding.viewModel = viewModel
        viewModel.binding = binding
        viewModel.compositeDisposable = this.compositeDisposable

        setContentView(binding.root)

        // Retrieve the last known location from SharedPreferences
        val lastLocation = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            .getString(LAST_LOCATION, null)

        if (lastLocation != null) {
            // Fetch weather data for the last known location
            viewModel.getLastLocationWeather(lastLocation)
        } else {
            // Request location permission from the user if no last known location is available
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
                // Location permission granted, retrieve the current location
                viewModel.getLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Location permission granted, retrieve the current location
                viewModel.getLocation()
            }
            else -> {
                // No location access granted.
            }
        }
    }
}
