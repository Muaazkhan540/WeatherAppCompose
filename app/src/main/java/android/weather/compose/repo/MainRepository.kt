package android.weather.compose.repo

import android.weather.compose.db.WeatherDao
import android.weather.compose.models.five.FiveDay
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.service.WeatherService
import android.weather.compose.utils.safeApiCall

class MainRepository(
    private val apiService: WeatherService, private val dao: WeatherDao,
) {
    suspend fun weatherForecast(lat: Double, lon: Double) = safeApiCall {
        apiService.weatherForecast(lat, lon)
    }

    suspend fun fiveDayWeather(lat: Double, lon: Double) = safeApiCall {
        apiService.fiveDay(lat, lon)
    }

    suspend fun ipAddress(ip: String?) = safeApiCall {
        apiService.ipAddress(ip)
    }

    fun addFiveDayWeather(fiveDay: FiveDay) {
        dao.addFiveDayWeather(fiveDay)
    }

    fun getWeatherForecast() = dao.getWeatherForecast()

    fun getFiveDayWeather() = dao.getFiveDayWeather()

    fun addWeatherForecast(weather: WeatherForecast) = dao.weatherForecast(weather)
}