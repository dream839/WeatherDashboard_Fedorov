package com.Fedorov.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fedorov.myapplication.data.WeatherData
import com.Fedorov.myapplication.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import java.lang.Exception

class WeatherViewModel: ViewModel() {
    private val repository = WeatherRepository()
    private val _weatherState = MutableStateFlow(WeatherData())
    val weatherState: StateFlow<WeatherData> = _weatherState.asStateFlow()

    init {
        loadWeatherData()
    }

    fun loadWeatherData() {
        viewModelScope.launch {
            _weatherState.value = _weatherState.value.copy(
                isLoading = true,
                error = null
            )
            try {
                val temperatureDeferred = async { repository.fetchTemperature() }
                val humidityDeferred = async { repository.fetchHumidity() }
                val windSpeedDeferred = async { repository.fetchWindSpeed() }
                val temperature = temperatureDeferred.await()
                val humidity = humidityDeferred.await()
                val windSpeed = windSpeedDeferred.await()
                _weatherState.value = WeatherData(
                    temperature = temperature,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {

//                val temperature = repository.fetchTemperature()
//                _weatherState.value = _weatherState.value.copy(temperature = temperature)
//                val humidity = repository.fetchTemperature()
//                _weatherState.value = _weatherState.value.copy(humidity = humidity)
//                val windSpeed = repository.fetchTemperature()
//                _weatherState.value = _weatherState.value.copy(windSpeed = windSpeed)
//                _weatherState.value = _weatherState.value.copy(isLoading = false)
//            } catch (e: Exception) {
//                _weatherState.value = _weatherState.value.copy(
//                    isLoading = false,
//                    error = "Ошибка загрузки: ${e.message}"
//                )
//            }
//        }
//    }
            }
        }
    }
}