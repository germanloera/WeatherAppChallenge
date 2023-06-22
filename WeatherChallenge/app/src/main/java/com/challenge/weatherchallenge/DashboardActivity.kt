package com.challenge.weatherchallenge


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.challenge.weatherchallenge.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)
    }
}