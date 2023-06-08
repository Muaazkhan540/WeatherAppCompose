package android.weather.compose.models.five


import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "five_day_weather")
data class FiveDay(
    @PrimaryKey(autoGenerate = false)
    var primaryKey: Long,
    @Embedded
    var city: City,
    var cnt: Int = 0,
    var cod: String = "",
    var list: ArrayList<Day>,
    var message: Int = 0,
)

@Keep
data class Day(
    var clouds: Clouds,
    var dt: Int = 0,
    var dt_txt: String = "",
    var main: Main,
    var pop: Double = .0,
    var visibility: Int = 0,
    var weather: List<Weather> = emptyList(),
    var wind: Wind,
    var showContainer: Boolean = false,
)

@Keep
data class City(
    var coord: Coord,
    var country: String = "",
    var id: Int = 0,
    var name: String = "",
    var population: Int = 0,
    var sunrise: Int = 0,
    var sunset: Int = 0,
    var timezone: Int = 0,
)

@Keep
data class Clouds(
    var all: Int = 0,
)

@Keep
data class Main(
    var feels_like: Double = .0,
    var grnd_level: Int = 0,
    var humidity: Int = 0,
    var pressure: Int = 0,
    var sea_level: Int = 0,
    var temp: Double = .0,
    var temp_kf: Double = .0,
    var temp_max: Double = .0,
    var temp_min: Double = .0,
)

@Keep

data class Wind(
    var deg: Int = 0,
    var gust: Double = .0,
    var speed: Double = .0,
)

@Keep
data class Weather(
    var icon: String = "",
    var description: String = ""
)

@Keep
data class Coord(
    var lat: Double = .0,
    var lon: Double = .0,
)

data class FiveDayState(
    var success: FiveDay? = null,
    var isLoading: Boolean = false,
    var error: String? = null
)