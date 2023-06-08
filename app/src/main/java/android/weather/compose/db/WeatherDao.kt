package android.weather.compose.db

import android.weather.compose.models.five.FiveDay
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.models.search.SearchItem
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeather(weather: SearchItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFiveDayWeather(fiveDay: FiveDay): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun weatherForecast(weather: WeatherForecast): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWidgetWeather(weather: WeatherForecast): Long

    @Query("SELECT * FROM city_weather")
    fun getAllWeather(): Flow<List<SearchItem?>>

    @Query("SELECT * FROM weather_forecast")
    fun getWeatherForecast(): Flow<WeatherForecast?>

    @Query("SELECT * FROM weather_forecast")
    fun getWidgetWeather(): Flow<WeatherForecast>

    @Query("SELECT * FROM five_day_weather")
    fun getFiveDayWeather(): Flow<FiveDay>

    @Query("select * from city_weather where name like :city")
    fun getOneWeather(city: String): SearchItem?

    @Query("SELECT * FROM city_weather WHERE name LIKE '%' || :query || '%'")
    fun getFilterCities(query: String): Flow<List<SearchItem>>

//    @Query("SELECT * FROM city_weather WHERE lat LIKE '%' || :lat || '%' and lon LIKE '%' || :lon || '%' ")
//    fun currentWeather(lat: Double, lon: Double): Flow<WeatherForecast>

    @Delete
    fun deleteWeather(weatherDetail: SearchItem)

    @Query("Delete FROM city_weather")
    fun deleteAll()
}