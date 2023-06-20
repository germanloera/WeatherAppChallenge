package com.challenge.weatherapp.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challenge.weatherapp.R
import com.challenge.weatherapp.ui.components.getWeatherCard
import com.challenge.weatherapp.viewModel.DashboardViewModel


@Composable
fun DashboardLayout(viewModel : DashboardViewModel) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color =  colorResource(id = R.color.clear))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.3f)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            getWeatherCard()
        }

}



}