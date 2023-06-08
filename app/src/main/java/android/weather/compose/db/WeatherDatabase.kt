package android.weather.compose.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.weather.compose.models.five.FiveDay
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.models.search.SearchItem

@Database(
    entities = [SearchItem::class, WeatherForecast::class, FiveDay::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}