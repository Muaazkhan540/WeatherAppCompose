package android.weather.compose.service

import android.weather.compose.BuildConfig.API_KEY
import android.weather.compose.BuildConfig.OPEN_KEY
import android.weather.compose.models.IpAddress
import android.weather.compose.models.five.FiveDay
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.utils.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class WeatherService {

    private val client = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        engine {
            connectTimeout = 100_000
            socketTimeout = 100_000
        }
    }

    suspend fun weatherForecast(lat: Double, lon: Double): WeatherForecast {
        return client.get {
            url("$BASE_URL/forecast.json")
            parameter("q", "${lat},$lon")
            parameter("key", API_KEY)
            parameter("aqi", "yes")
            parameter("days", 3)
        }
    }

    suspend fun fiveDay(lat: Double, lon: Double): FiveDay {
        return client.get {
            url("https://api.openweathermap.org/data/2.5/forecast")
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", OPEN_KEY)
            parameter("units", "metric")
        }
    }

    suspend fun ipAddress(ip: String?): IpAddress {
        return client.get {
            url("http://ip-api.com/json/$ip")
        }
    }
}