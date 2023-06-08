package android.weather.compose.utils

import android.weather.compose.setting.Precipitation
import android.weather.compose.setting.Pressure
import android.weather.compose.setting.Temperature
import android.weather.compose.setting.Visibility
import android.weather.compose.setting.Wind
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Double.setTemperature(temperature: Temperature): String {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = 0
    return when (temperature) {
        Temperature.Centigrade -> "${nf.format(this) ?: "_"}°"
        Temperature.Fahrenheit -> "${nf.format(this * 9 / 5 + 32) ?: "_"}°"
        Temperature.Kelvin -> nf.format(this + 273.15) ?: "_"
    }
}

fun Int.setVisibility(visibility: Visibility): String {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = 0
    return when (visibility) {
        Visibility.KILOMETER -> "${nf.format(this * 0.001)} km"
        Visibility.MILES -> "${nf.format(this * 0.001 / 1.609)} mi"
    }
}

fun Int.setPressure(pressure: Pressure): String {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = 0
    return when (pressure) {
        Pressure.PoundForcePerSquareInch -> "${nf.format(this * 0.0145)}psi"
        Pressure.MillibarPressureUnit -> "${nf.format(this)}mBar"
        Pressure.InchOfMercury -> "${nf.format(this / 33.864)}inHg"
        Pressure.MillimetersOfMercury -> "${nf.format(this / 1.333)}mmHg"
    }
}

fun Double.setPressure(pressure: Pressure): String {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = 0
    return when (pressure) {
        Pressure.PoundForcePerSquareInch -> "${nf.format(this * 0.0145)}psi"
        Pressure.MillibarPressureUnit -> "${nf.format(this)}mBar"
        Pressure.InchOfMercury -> "${nf.format(this / 33.864)}inHg"
        Pressure.MillimetersOfMercury -> "${nf.format(this / 1.333)}mmHg"
    }
}

fun Double.setVisibility(visibility: Visibility): String {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = 0
    return when (visibility) {
        Visibility.KILOMETER -> "${nf.format(this)} km"
        Visibility.MILES -> "${nf.format(this / 1.609)} mi"
    }
}

fun Double.setWindSpeed(speed: Wind): String {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = 0
    return when (speed) {
        Wind.KilometerPerHour -> "${nf.format(this * 3.6)} km/h"
        Wind.MeterPerSec -> "${nf.format(this)} m/s"
        Wind.MilesPerHour -> "${nf.format(this * 1.609)} mi/h"
    }
}

fun Double.stringFormat(fraction: Number = 0): String {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = fraction.toInt()
    return nf.format(this).toString()
}

fun Double?.progressFormat(fraction: Int = 2): Double {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = fraction
    return try {
        (nf.format(this?.div(100.0) ?: 0.25).toString().replace("٠٫", ".").toDouble())
    } catch (e: Exception) {
        0.25
    }
}

fun Double.setCurrentWindSpeed(speed: Wind): String {
    val nf: NumberFormat = DecimalFormat.getInstance()
    nf.maximumFractionDigits = 0
    return when (speed) {
        Wind.KilometerPerHour -> nf.format(this)
        Wind.MeterPerSec -> nf.format(this / 3.6)
        Wind.MilesPerHour -> nf.format(this / 1.609)
    }
}

fun Double.setRain(precipitation: Precipitation): String {
    return when (precipitation) {
        Precipitation.Millimeter -> "${this.stringFormat(3)} mm"
        Precipitation.Inches -> "${(this / 25.4).stringFormat(3)} in"
    }
}

val Wind.windSymbol: String
    get() = when (this) {
        Wind.KilometerPerHour -> "km/h"
        Wind.MeterPerSec -> "m/s"
        Wind.MilesPerHour -> "mi/h"
    }
val Precipitation.rainSymbol: String
    get() = when (this) {
        Precipitation.Millimeter -> "(mm)"
        Precipitation.Inches -> "(in)"
    }

const val CURRENT_FORMAT = "d MMMM, hh:mm a"
const val TIME_FORMAT = "hh:mm a"
const val SUN_FORMAT = "HH:mm"
const val DAY_FORMAT = "EEEE, dd MMM"
const val DAILY_DETAIL_FORMAT = "EEEE"
const val TOMORROW_FORMAT = "dd MMMM"
const val REGION_FORMAT = "yyyy-MM-dd"
const val SUN_CONVERTER_FORMAT = "yyyy-MM-dd hh:mm a"
const val HOUR_FORMAT = "h a"

fun Number.timeConverter(format: String, zone: String = defaultZoneId): String {
    val time = SimpleDateFormat(format, Locale.getDefault())
    time.timeZone = TimeZone.getTimeZone(zone)
    val symbols = DateFormatSymbols(Locale.getDefault())
    symbols.amPmStrings = arrayOf("am", "pm")
    time.dateFormatSymbols = symbols
    return time.format(this.toLong() * 1000L)
}

fun Long.sunConverter(format: String, zone: String): String {
    val time = SimpleDateFormat(format, Locale.getDefault())
    time.timeZone = TimeZone.getTimeZone(zone)
    return time.format(this)
}

fun currentTimeSun(zone: String): String {
    val sdf = SimpleDateFormat(REGION_FORMAT, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(zone)
    return sdf.format(Date())
}

fun String.dateToMillis(zone: String, format: String = SUN_CONVERTER_FORMAT): Long? {
    val givenDateString = "${currentTimeSun(zone)} $this"
    val sdf = SimpleDateFormat(format, Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone(zone)
    return try {
        val mDate = sdf.parse(givenDateString)
        mDate?.time
    } catch (e: ParseException) {
        0
    }
}