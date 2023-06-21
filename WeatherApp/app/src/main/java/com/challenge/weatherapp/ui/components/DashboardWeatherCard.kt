package com.challenge.weatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun getWeatherCard(icon: String = "10d", temp: String = "30", city: String = "") {
    val iconUrl = "https://openweathermap.org/img/wn/${icon}@4x.png"
    val paddingModifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
    val elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    Card(elevation = elevation, modifier = paddingModifier, shape = RoundedCornerShape(20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GlideImage(
                model = iconUrl, contentDescription = "",
                modifier = Modifier.fillMaxWidth(fraction = 0.5f)
            ) {
                it.fitCenter()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,

                ) {
                Column() {


                    Text(
                        text = temp,
                        textAlign = TextAlign.Center,
                        fontSize = 80.sp
                    )
                    Text(text = city, fontSize = 20.sp)
                }
            }
        }


    }


}








