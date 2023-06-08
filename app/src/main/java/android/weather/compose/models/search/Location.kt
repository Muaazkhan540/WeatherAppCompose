package android.weather.compose.models.search


import androidx.annotation.Keep

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