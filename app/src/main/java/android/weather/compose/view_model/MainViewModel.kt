package android.weather.compose.view_model

import android.weather.compose.models.five.FiveDay
import android.weather.compose.models.five.FiveDayState
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.models.forecast.WeatherState
import android.weather.compose.repo.MainRepository
import android.weather.compose.ui.theme.DayColor
import android.weather.compose.utils.ResponseResult
import android.weather.compose.utils.handler
import android.weather.compose.utils.liveResponse
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    init {
        viewModelScope.launch(handler + Dispatchers.IO + SupervisorJob()) {
            launch(Dispatchers.IO) {
                coordinates.collect {
                    currentWeather(it.first, it.second)
                    fiveDayWeather(it.first, it.second)
                }
            }
            launch(Dispatchers.IO) {
                publicIp.collect {
                    ipAddress(it)
                }
            }
        }
    }

    var language by mutableStateOf("en")
    var publicIp = MutableStateFlow("")
    var coordinates = MutableStateFlow(Pair(0.0, 0.0))
    var currentCoordinates = MutableStateFlow(Pair(.0, .0))
    var ipCoordinates = MutableStateFlow(Pair(.0, .0))
    var location = MutableStateFlow(Pair("", ""))

    var weatherState = MutableStateFlow(WeatherState())

    var color by mutableStateOf(DayColor)

    var fiveDayState by mutableStateOf(FiveDayState())
        private set

    fun currentWeather(lat: Double, lon: Double) = with(liveResponse {
        repository.weatherForecast(lat, lon)
    }) {
        viewModelScope.launch(handler + Dispatchers.IO) {
            this@with.collect {
                when (it) {
                    is ResponseResult.Failure -> {
                        getWeatherForecast().collect { weather ->
                            weatherState.value = weatherState.value.copy(
                                success = weather,
                                isLoading = false,
                                error = it.error.message
                            )
                        }
                    }

                    is ResponseResult.Loading -> {
                        weatherState.value = weatherState.value.copy(
                            success = null,
                            isLoading = true,
                            error = null
                        )
                    }

                    is ResponseResult.Success -> {
                        weatherState.value = weatherState.value.copy(
                            success = it.data,
                            isLoading = false,
                            error = null
                        )
                        addWeatherForecast(it.data)
                    }
                }
            }
        }
    }

    fun fiveDayWeather(lat: Double, lon: Double) = with(liveResponse {
        repository.fiveDayWeather(lat, lon)
    }) {
        viewModelScope.launch(handler + Dispatchers.IO) {
            this@with.collect {
                when (it) {
                    is ResponseResult.Failure -> {
                        repository.getFiveDayWeather().collect { five ->
                            fiveDayState = fiveDayState.copy(
                                success = five,
                                isLoading = false,
                                error = it.error.message
                            )
                        }
                    }

                    is ResponseResult.Loading -> {
                        fiveDayState = fiveDayState.copy(
                            success = null,
                            isLoading = true,
                            error = null
                        )
                    }

                    is ResponseResult.Success -> {
                        fiveDayState = fiveDayState.copy(
                            success = it.data,
                            isLoading = false,
                            error = null
                        )
                        addFiveDayWeather(it.data)
                    }
                }
            }
        }
    }

    private fun addFiveDayWeather(fiveDay: FiveDay) =
        viewModelScope.launch(handler + Dispatchers.IO) {
            repository.addFiveDayWeather(fiveDay)
        }

    private fun addWeatherForecast(weather: WeatherForecast) =
        viewModelScope.launch(handler + Dispatchers.IO) {
            repository.addWeatherForecast(weather)
        }

    private fun getWeatherForecast() = repository.getWeatherForecast()

    private fun ipAddress(ip: String?) = with(liveResponse { repository.ipAddress(ip) }) {
        viewModelScope.launch(handler + Dispatchers.IO) {
            this@with.collect {
                when (it) {
                    is ResponseResult.Failure -> {}
                    is ResponseResult.Loading -> {}
                    is ResponseResult.Success -> {
                        val lat = it.data.lat
                        val lon = it.data.lon
                        ipCoordinates.value = Pair(lat, lon)
                        location.value = Pair("", it.data.city)
                    }
                }
            }
        }
    }
}