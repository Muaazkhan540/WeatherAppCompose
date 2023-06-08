package android.weather.compose.models.forecast


import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "weather_forecast")
data class WeatherForecast(
    @Embedded
    var current: Current? = null,
    @Embedded
    var forecast: ForecastX? = null,
    @Embedded
    var location: Location? = null,
    @PrimaryKey(autoGenerate = false)
    val weatherPrimaryKey: Long,
)

@Keep
data class Current(
    var air_quality: AirQuality?,
    var cloud: Int = 0,
    var condition: Condition,
    var feelslike_c: Double = .0,
    var feelslike_f: Double = .0,
    var gust_kph: Double = .0,
    var gust_mph: Double = .0,
    var humidity: Int = 0,
    var is_day: Int = 0,
    var last_updated: String = "",
    var last_updated_epoch: Int = 0,
    var precip_in: Double = .0,
    var precip_mm: Double = .0,
    var pressure_in: Double = .0,
    var pressure_mb: Double = .0,
    var temp_c: Double = .0,
    var temp_f: Double = .0,
    var uv: Double = .0,
    var vis_km: Double = .0,
    var vis_miles: Double = .0,
    var wind_degree: Int = 0,
    var wind_dir: String = "",
    var wind_kph: Double = .0,
    var wind_mph: Double = .0,
)

@Keep
data class AirQuality(
    var co: Double?,
    /* @SerializedName("gb-defra-index")
     var gb_index: Int? = 0,*/
    var no2: Double? = .0,
    var o3: Double? = .0,
    var pm10: Double? = .0,
    var pm2_5: Double? = .0,
    var so2: Double? = .0,
    /*@SerializedName("us-epa-index")
    var us_index: Int? = 0*/
)

@Keep
data class Condition(
    var code: Int = 0,
    var icon: String = "",
    var text: String = "",
)

@Keep
data class ForecastX(
    var forecastday: ArrayList<Forecastday>?,
)

@Keep
data class Forecastday(
    var astro: Astro,
    var date: String = "",
    var date_epoch: Int = 0,
    var day: Day,
    var hour: ArrayList<Hour>?,
)

@Keep
data class Astro(
    var moon_illumination: String = "",
    var moon_phase: String = "",
    var moonrise: String = "",
    var moonset: String = "",
    var sunrise: String = "",
    var sunset: String = "",
)

@Keep
data class Day(
    var avghumidity: Double = .0,
    var avgtemp_c: Double = .0,
    var avgtemp_f: Double = .0,
    var avgvis_km: Double = .0,
    var avgvis_miles: Double = .0,
    var condition: Condition,
    var daily_chance_of_rain: Int = 0,
    var daily_chance_of_snow: Int = 0,
    var daily_will_it_rain: Int = 0,
    var daily_will_it_snow: Int = 0,
    var maxtemp_c: Double = .0,
    var maxtemp_f: Double = .0,
    var maxwind_kph: Double = .0,
    var maxwind_mph: Double = .0,
    var mintemp_c: Double = .0,
    var mintemp_f: Double = .0,
    var totalprecip_in: Double = .0,
    var totalprecip_mm: Double = .0,
    var uv: Double = .0,
)

@Keep
data class Hour(
    var chance_of_rain: Int = 0,
    var chance_of_snow: Int = 0,
    var cloud: Int = 0,
    var condition: Condition,
    var dewpoint_c: Double = .0,
    var dewpoint_f: Double = .0,
    var feelslike_c: Double = .0,
    var feelslike_f: Double = .0,
    var gust_kph: Double = .0,
    var gust_mph: Double = .0,
    var heatindex_c: Double = .0,
    var heatindex_f: Double = .0,
    var humidity: Int = 0,
    var is_day: Int = 0,
    var precip_in: Double = .0,
    var precip_mm: Double = .0,
    var pressure_in: Double = .0,
    var pressure_mb: Double = .0,
    var temp_c: Double = .0,
    var temp_f: Double = .0,
    var time: String? = "",
    var time_epoch: Int = 0,
    var uv: Double = .0,
    var vis_km: Double = .0,
    var vis_miles: Double = .0,
    var will_it_rain: Int = 0,
    var will_it_snow: Int = 0,
    var wind_degree: Int = 0,
    var wind_dir: String = "",
    var wind_kph: Double = .0,
    var wind_mph: Double = .0,
    var windchill_c: Double = .0,
    var windchill_f: Double = .0,
)

@Keep
data class Location(
    var country: String = "",
    var lat: Double = .0,
    var localtime: String = "",
    var localtime_epoch: Int = 0,
    var lon: Double = .0,
    var name: String = "",
    var region: String = "",
    var tz_id: String = "",
)

data class WeatherState(
    var success: WeatherForecast? = null,
    var isLoading: Boolean = false,
    var error: String? = null,
)