package android.weather.compose.models.search


import androidx.annotation.Keep

@Keep
data class Current(
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