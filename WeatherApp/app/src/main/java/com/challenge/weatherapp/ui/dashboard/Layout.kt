package com.challenge.weatherapp.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.challenge.weatherapp.R
import com.challenge.weatherapp.ui.components.getWeatherCard
import com.challenge.weatherapp.ui.components.locationCard
import com.challenge.weatherapp.ui.components.locationList
import com.challenge.weatherapp.viewModel.DashboardViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardLayout(viewModel: DashboardViewModel) {
    val weatherState by viewModel.weatherResponse.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.clear))
    ) {

        Column() {


            val icon = weatherState.weather?.first()?.icon ?: ""
            val temp = weatherState.main?.F() ?: ""
            var city = weatherState.city()

            weatherState.label?.let {
                city = it
            }

            weatherState.weather?.first()?.icon?.let {
                getWeatherCard(
                    icon = icon,
                    temp = temp,
                    city = city
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                TextField(
                    value = viewModel.cityTextFieldValue.collectAsState().value,
                    isError = true,
                    onValueChange = {
                        viewModel.onChangeCityText(it)
                    })


                Button(
                    modifier = Modifier.padding(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    onClick = {
                    viewModel.searchCity()
                })
                {
                    Image(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "search"
                    )

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ){
                locationList(viewModel)
            }

        }
    }


}