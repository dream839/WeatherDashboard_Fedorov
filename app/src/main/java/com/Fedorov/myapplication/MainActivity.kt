package com.Fedorov.myapplication

import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.Fedorov.myapplication.ui.theme.WeatherDashboardTheme
import com.Fedorov.myapplication.viewmodel.WeatherViewModel
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherDashboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WetherDashboardScreen()
                }
            }
        }
    }
}
@Composable
fun WetherDashboardScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val wetherState by viewModel.weatherState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Weather Dashboard",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        WeatherCard(
            emoji = "❗",
            title = "Temperature",
            value = wetherState.temperature?.let { "$it C" }?: "-",
            isLoading = wetherState.isLoading && wetherState.temperature == null
        )
        Spacer(modifier = Modifier.height(32.dp))
        WeatherCard(
            emoji = "💦",
            title = "Humidity",
            value = wetherState.humidity?.let { "$it %" }?: "-",
            isLoading = wetherState.isLoading && wetherState.humidity == null
        )
        Spacer(modifier = Modifier.height(32.dp))
        WeatherCard(
            emoji = "🌪",
            title = "Wind Speed",
            value = wetherState.windSpeed?.let { "$it m/s" }?: "-",
            isLoading = wetherState.isLoading && wetherState.windSpeed == null
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {viewModel.loadWeatherData()},
            enabled = !wetherState.isLoading
        ) {
            Text(text = if (wetherState.isLoading)"Loading..." else "🔄 Refresh" +
                    "Weather")
        }
        if (wetherState.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = wetherState.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (wetherState.loadingProgress.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = wetherState.loadingProgress,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
@Composable
fun WeatherCard(
    emoji: String,
    title: String,
    value: String,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}