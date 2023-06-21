package com.challenge.weatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.challenge.weatherapp.viewModel.DashboardViewModel


@Composable
fun locationList(viewModel: DashboardViewModel) {

    val list = viewModel.locationList.collectAsState().value

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(list.size) {
            locationCard(list[it].locationName()) {
                viewModel.loadCityWeather(list[it])
            }

        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun locationCard(city: String = "Chetumal, MX", onClick: (() -> Unit) = {}) {
    val paddingModifier = Modifier
        .padding(10.dp)
        .height(50.dp)
        .fillMaxWidth()
    val elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    Box() {
        Card(
            elevation = elevation,
            modifier = paddingModifier,
            shape = RoundedCornerShape(10.dp),
            onClick = { onClick.invoke() }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp), verticalArrangement = Arrangement.Center
            ) {


                Text(text = city, fontSize = 20.sp)
            }
        }
    }

}
