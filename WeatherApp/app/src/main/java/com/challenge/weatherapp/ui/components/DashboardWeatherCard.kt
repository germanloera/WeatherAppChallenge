package com.challenge.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.challenge.weatherapp.R


@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun getWeatherCard() {
    val paddingModifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.broken_clouds))
    val elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    Card(elevation = elevation, modifier = paddingModifier, shape = RoundedCornerShape(20.dp)) {
        Row {
            GlideImage(
                model = "https://openweathermap.org/img/wn/10d@4x.png", contentDescription = "",
                modifier = Modifier.fillMaxWidth(fraction = 0.5f)
            ) {
                it.fitCenter()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "35º",

                    textAlign = TextAlign.Center,
                    fontSize = 80.sp
                )
            }
        }

    }


}