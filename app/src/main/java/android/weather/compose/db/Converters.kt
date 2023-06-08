package android.weather.compose.db

import android.weather.compose.models.five.Coord
import android.weather.compose.models.five.Day
import android.weather.compose.models.forecast.AirQuality
import android.weather.compose.models.forecast.Condition
import android.weather.compose.models.forecast.Forecastday
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {
    @TypeConverter
    fun fromCondition(condition: android.weather.compose.models.search.Condition): Int {
        return condition.code
    }

    @TypeConverter
    fun toCondition(code: Int): android.weather.compose.models.search.Condition {
        return android.weather.compose.models.search.Condition(code)
    }

    @TypeConverter
    fun fromConditionToString(condition: Condition): String {
        return Gson().toJson(condition)
    }

    @TypeConverter
    fun fromStringToCondition(json: String): Condition {
        return Gson().fromJson(
            json, Condition::class.java
        )
    }

    @TypeConverter
    fun fromString(value: String): ArrayList<Forecastday?>? {
        val listType: Type = object : TypeToken<ArrayList<Forecastday?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Forecastday?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromCordToString(cord: Coord): String {
        return Gson().toJson(cord)
    }

    @TypeConverter
    fun fromStringToCord(cord: String): Coord {
        return Gson().fromJson(cord, Coord::class.java)
    }

    @TypeConverter
    fun fromDay(value: String?): ArrayList<Day?>? {
        val listType: Type = object : TypeToken<ArrayList<Day?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toDay(list: ArrayList<Day?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromAirQuality(airQuality: AirQuality): String {
        return Gson().toJson(airQuality)
    }

    @TypeConverter
    fun toAirQuality(airQuality: String): AirQuality {
        return Gson().fromJson(airQuality, AirQuality::class.java)
    }
}