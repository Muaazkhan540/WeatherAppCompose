package android.weather.compose.setting

import android.content.Context
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable

val Context.dataStore by dataStore("app-settings.json", AppSettingsSerializer)

@Serializable
data class AppSettings(
    val temperature: Temperature = Temperature.Centigrade,
    val wind: Wind = Wind.KilometerPerHour,
    val pressure: Pressure = Pressure.MillibarPressureUnit,
    val visibility: Visibility = Visibility.KILOMETER,
    val precipitation: Precipitation = Precipitation.Millimeter,
    val notification: Long = 8L,
    val theme: Boolean = false,
    val isChecked: Boolean = false,
)

enum class Temperature {
    Centigrade, Fahrenheit, Kelvin
}

enum class Wind {
    KilometerPerHour, MeterPerSec, MilesPerHour
}

enum class Visibility {
    KILOMETER, MILES
}

enum class Pressure {
    PoundForcePerSquareInch, MillibarPressureUnit, InchOfMercury, MillimetersOfMercury
}

enum class Precipitation {
    Millimeter, Inches
}

suspend fun Context.updateTemperature(temperature: Temperature) {
    dataStore.updateData {
        it.copy(temperature = temperature)
    }
}

suspend fun Context.updatePrecipitation(precipitation: Precipitation) {
    dataStore.updateData {
        it.copy(precipitation = precipitation)
    }
}

suspend fun Context.updatePressure(pressure: Pressure) {
    dataStore.updateData {
        it.copy(pressure = pressure)
    }
}

suspend fun Context.updateWind(wind: Wind) {
    dataStore.updateData {
        it.copy(wind = wind)
    }
}

suspend fun Context.updateVisibility(visibility: Visibility) {
    dataStore.updateData {
        it.copy(visibility = visibility)
    }
}

suspend fun Context.updateNotification(notification: Long) {
    dataStore.updateData {
        it.copy(notification = notification)
    }
}