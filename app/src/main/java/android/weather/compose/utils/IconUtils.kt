package android.weather.compose.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.weather.compose.R
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

fun Context.color(@ColorRes resIdRes: Int) = ResourcesCompat.getColor(resources, resIdRes, null)

fun Context.drawable(@DrawableRes resIdRes: Int) =
    ResourcesCompat.getDrawable(resources, resIdRes, null)

fun Context.typeFace(@FontRes resIdRes: Int) = ResourcesCompat.getFont(this, resIdRes)

fun setBackgroundDay(code: Int): Int {
    return when (code) {
        1000 -> R.drawable.clear_day_bg
        1003 -> R.drawable.cloudy_rainy_day_bg
        1006 -> R.drawable.cloudy_rainy_day_bg
        1009 -> R.drawable.cloudy_rainy_day_bg
        1030 -> R.drawable.haze_bg
        1063 -> R.drawable.cloudy_rainy_day_bg
        1066 -> R.drawable.winter_day_bg
        1069 -> R.drawable.winter_day_bg
        1072 -> R.drawable.cloudy_rainy_day_bg
        1087 -> R.drawable.cloudy_rainy_day_bg
        1114 -> R.drawable.winter_day_bg
        1117 -> R.drawable.winter_day_bg
        1135 -> R.drawable.haze_bg
        1147 -> R.drawable.haze_bg
        1150 -> R.drawable.cloudy_rainy_day_bg
        1153 -> R.drawable.cloudy_rainy_day_bg
        1168 -> R.drawable.cloudy_rainy_day_bg
        1171 -> R.drawable.cloudy_rainy_day_bg
        1180 -> R.drawable.cloudy_rainy_day_bg
        1183 -> R.drawable.cloudy_rainy_day_bg
        1186 -> R.drawable.cloudy_rainy_day_bg
        1189 -> R.drawable.cloudy_rainy_day_bg
        1192 -> R.drawable.cloudy_rainy_day_bg
        1195 -> R.drawable.cloudy_rainy_day_bg
        1198 -> R.drawable.cloudy_rainy_day_bg
        1201 -> R.drawable.cloudy_rainy_day_bg
        1204 -> R.drawable.winter_day_bg
        1207 -> R.drawable.winter_day_bg
        1210 -> R.drawable.winter_day_bg
        1213 -> R.drawable.winter_day_bg
        1216 -> R.drawable.winter_day_bg
        1219 -> R.drawable.winter_day_bg
        1222 -> R.drawable.winter_day_bg
        1225 -> R.drawable.winter_day_bg
        1237 -> R.drawable.winter_day_bg
        1240 -> R.drawable.cloudy_rainy_day_bg
        1243 -> R.drawable.cloudy_rainy_day_bg
        1246 -> R.drawable.cloudy_rainy_day_bg
        1249 -> R.drawable.winter_day_bg
        1252 -> R.drawable.winter_day_bg
        1255 -> R.drawable.winter_day_bg
        1258 -> R.drawable.winter_day_bg
        1261 -> R.drawable.winter_day_bg
        1264 -> R.drawable.winter_day_bg
        1273 -> R.drawable.cloudy_rainy_day_bg
        1276 -> R.drawable.cloudy_rainy_day_bg
        1279 -> R.drawable.cloudy_rainy_day_bg
        1282 -> R.drawable.cloudy_rainy_day_bg
        else -> R.drawable.clear_day_bg
    }
}

fun setBackgroundImageDay(code: Int): Int {
    return when (code) {
        1000 -> R.drawable.clear_day_image
        1003 -> R.drawable.cloudy_day_image
        1006 -> R.drawable.cloudy_day_image
        1009 -> R.drawable.cloudy_day_image
        1030 -> R.drawable.haze_image
        1063 -> R.drawable.rainy_day_image
        1066 -> R.drawable.snow_day_image
        1069 -> R.drawable.snow_day_image
        1072 -> R.drawable.rainy_day_image
        1087 -> R.drawable.rainy_day_image
        1114 -> R.drawable.snow_day_image
        1117 -> R.drawable.snow_day_image
        1135 -> R.drawable.haze_image
        1147 -> R.drawable.haze_image
        1150 -> R.drawable.rainy_day_image
        1153 -> R.drawable.rainy_day_image
        1168 -> R.drawable.rainy_day_image
        1171 -> R.drawable.rainy_day_image
        1180 -> R.drawable.rainy_day_image
        1183 -> R.drawable.rainy_day_image
        1186 -> R.drawable.rainy_day_image
        1189 -> R.drawable.rainy_day_image
        1192 -> R.drawable.rainy_day_image
        1195 -> R.drawable.rainy_day_image
        1198 -> R.drawable.rainy_day_image
        1201 -> R.drawable.rainy_day_image
        1204 -> R.drawable.snow_day_image
        1207 -> R.drawable.snow_day_image
        1210 -> R.drawable.snow_day_image
        1213 -> R.drawable.snow_day_image
        1216 -> R.drawable.snow_day_image
        1219 -> R.drawable.snow_day_image
        1222 -> R.drawable.snow_day_image
        1225 -> R.drawable.snow_day_image
        1237 -> R.drawable.snow_day_image
        1240 -> R.drawable.rainy_day_image
        1243 -> R.drawable.rainy_day_image
        1246 -> R.drawable.rainy_day_image
        1249 -> R.drawable.snow_day_image
        1252 -> R.drawable.snow_day_image
        1255 -> R.drawable.snow_day_image
        1258 -> R.drawable.snow_day_image
        1261 -> R.drawable.snow_day_image
        1264 -> R.drawable.snow_day_image
        1273 -> R.drawable.rainy_day_image
        1276 -> R.drawable.rainy_day_image
        1279 -> R.drawable.rainy_day_image
        1282 -> R.drawable.rainy_day_image
        else -> R.drawable.clear_day_image
    }
}

fun setBackgroundImageNight(code: Int): Int {
    return when (code) {
        1000 -> R.drawable.clear_night_image
        1003 -> R.drawable.cloudy_night_image
        1006 -> R.drawable.cloudy_night_image
        1009 -> R.drawable.cloudy_night_image
        1030 -> R.drawable.haze_image
        1063 -> R.drawable.rainy_night_image
        1066 -> R.drawable.snow_night_image
        1069 -> R.drawable.snow_night_image
        1072 -> R.drawable.rainy_night_image
        1087 -> R.drawable.rainy_night_image
        1114 -> R.drawable.snow_night_image
        1117 -> R.drawable.snow_night_image
        1135 -> R.drawable.haze_image
        1147 -> R.drawable.haze_image
        1150 -> R.drawable.rainy_night_image
        1153 -> R.drawable.rainy_night_image
        1168 -> R.drawable.rainy_night_image
        1171 -> R.drawable.rainy_night_image
        1180 -> R.drawable.rainy_night_image
        1183 -> R.drawable.rainy_night_image
        1186 -> R.drawable.rainy_night_image
        1189 -> R.drawable.rainy_night_image
        1192 -> R.drawable.rainy_night_image
        1195 -> R.drawable.rainy_night_image
        1198 -> R.drawable.rainy_night_image
        1201 -> R.drawable.rainy_night_image
        1204 -> R.drawable.snow_night_image
        1207 -> R.drawable.snow_night_image
        1210 -> R.drawable.snow_night_image
        1213 -> R.drawable.snow_night_image
        1216 -> R.drawable.snow_night_image
        1219 -> R.drawable.snow_night_image
        1222 -> R.drawable.snow_night_image
        1225 -> R.drawable.snow_night_image
        1237 -> R.drawable.snow_night_image
        1240 -> R.drawable.rainy_night_image
        1243 -> R.drawable.rainy_night_image
        1246 -> R.drawable.rainy_night_image
        1249 -> R.drawable.snow_night_image
        1252 -> R.drawable.snow_night_image
        1255 -> R.drawable.snow_night_image
        1258 -> R.drawable.snow_night_image
        1261 -> R.drawable.snow_night_image
        1264 -> R.drawable.snow_night_image
        1273 -> R.drawable.rainy_night_image
        1276 -> R.drawable.rainy_night_image
        1279 -> R.drawable.rainy_night_image
        1282 -> R.drawable.rainy_night_image
        else -> R.drawable.clear_night_image
    }
}

fun setBackgroundNight(code: Int): Int {
    return when (code) {
        1000 -> R.drawable.clear_night_bg
        1003 -> R.drawable.cloudy_night_bg
        1006 -> R.drawable.cloudy_night_bg
        1009 -> R.drawable.cloudy_night_bg
        1030 -> R.drawable.haze_bg
        1063 -> R.drawable.cloudy_night_bg
        1066 -> R.drawable.polar_night_bg
        1069 -> R.drawable.polar_night_bg
        1072 -> R.drawable.polar_night_bg
        1087 -> R.drawable.cloudy_night_bg
        1114 -> R.drawable.polar_night_bg
        1117 -> R.drawable.polar_night_bg
        1135 -> R.drawable.haze_bg
        1147 -> R.drawable.haze_bg
        1150 -> R.drawable.cloudy_night_bg
        1153 -> R.drawable.cloudy_night_bg
        1168 -> R.drawable.cloudy_night_bg
        1171 -> R.drawable.cloudy_night_bg
        1180 -> R.drawable.cloudy_night_bg
        1183 -> R.drawable.cloudy_night_bg
        1186 -> R.drawable.cloudy_night_bg
        1189 -> R.drawable.cloudy_night_bg
        1192 -> R.drawable.cloudy_night_bg
        1195 -> R.drawable.cloudy_night_bg
        1198 -> R.drawable.cloudy_night_bg
        1201 -> R.drawable.cloudy_night_bg
        1204 -> R.drawable.polar_night_bg
        1207 -> R.drawable.polar_night_bg
        1210 -> R.drawable.polar_night_bg
        1213 -> R.drawable.polar_night_bg
        1216 -> R.drawable.polar_night_bg
        1219 -> R.drawable.polar_night_bg
        1222 -> R.drawable.polar_night_bg
        1225 -> R.drawable.polar_night_bg
        1237 -> R.drawable.polar_night_bg
        1240 -> R.drawable.cloudy_night_bg
        1243 -> R.drawable.cloudy_night_bg
        1246 -> R.drawable.cloudy_night_bg
        1249 -> R.drawable.cloudy_night_bg
        1252 -> R.drawable.polar_night_bg
        1255 -> R.drawable.polar_night_bg
        1258 -> R.drawable.polar_night_bg
        1261 -> R.drawable.polar_night_bg
        1264 -> R.drawable.polar_night_bg
        1273 -> R.drawable.cloudy_night_bg
        1276 -> R.drawable.cloudy_night_bg
        1279 -> R.drawable.cloudy_night_bg
        1282 -> R.drawable.cloudy_night_bg
        else -> R.drawable.clear_night_bg
    }
}

fun setIconWidget(icon: String): Int {
    return when (icon) {
        "01d" -> (R.drawable.ic_sunny)
        "01n" -> (R.drawable.ic_clear_sky)
        "02d" -> (R.drawable.ic_few_clouds)
        "02n" -> (R.drawable.ic_few_clouds_night)
        "03d" -> (R.drawable.ic_clouds)
        "03n" -> (R.drawable.ic_clouds)
        "04d" -> (R.drawable.ic_mostly_clouds_day)
        "04n" -> (R.drawable.ic_mostly_clouds_night)
        "09d" -> (R.drawable.ic_rain)
        "09n" -> (R.drawable.ic_rain)
        "10d" -> (R.drawable.ic_rain)
        "10n" -> (R.drawable.ic_rain)
        "11d" -> (R.drawable.ic_thunderstorm)
        "11n" -> (R.drawable.ic_thunderstorm)
        "13d" -> (R.drawable.ic_snow)
        "13n" -> (R.drawable.ic_snow)
        "50d" -> (R.drawable.ic_mist)
        "50n" -> (R.drawable.ic_mist)
        else -> (R.drawable.ic_sunny)
    }
}

fun Context.setIconDay(code: Int): Drawable? {
    return when (code) {
        1000 -> drawable(R.drawable.ic_sunny)
        1003 -> drawable(R.drawable.ic_few_clouds)
        1006 -> drawable(R.drawable.ic_clouds)
        1009 -> drawable(R.drawable.ic_mostly_clouds_day)
        1030 -> drawable(R.drawable.ic_mist)
        1063 -> drawable(R.drawable.ic_rain)
        1066 -> drawable(R.drawable.ic_snow)
        1069 -> drawable(R.drawable.ic_sleet)
        1072 -> drawable(R.drawable.ic_drizzle)
        1087 -> drawable(R.drawable.ic_thunderstorm)
        1114 -> drawable(R.drawable.ic_snow)
        1117 -> drawable(R.drawable.ic_blizzard)
        1135 -> drawable(R.drawable.ic_mist)
        1147 -> drawable(R.drawable.ic_mist)
        1150 -> drawable(R.drawable.ic_rain)
        1153 -> drawable(R.drawable.ic_drizzle)
        1168 -> drawable(R.drawable.ic_drizzle)
        1171 -> drawable(R.drawable.ic_drizzle)
        1180 -> drawable(R.drawable.ic_rain)
        1183 -> drawable(R.drawable.ic_rain)
        1186 -> drawable(R.drawable.ic_rain)
        1189 -> drawable(R.drawable.ic_rain)
        1192 -> drawable(R.drawable.ic_rain)
        1195 -> drawable(R.drawable.ic_rain)
        1198 -> drawable(R.drawable.ic_rain)
        1201 -> drawable(R.drawable.ic_rain)
        1204 -> drawable(R.drawable.ic_sleet)
        1207 -> drawable(R.drawable.ic_sleet)
        1210 -> drawable(R.drawable.ic_snow)
        1213 -> drawable(R.drawable.ic_snow)
        1216 -> drawable(R.drawable.ic_snow)
        1219 -> drawable(R.drawable.ic_snow)
        1222 -> drawable(R.drawable.ic_snow)
        1225 -> drawable(R.drawable.ic_snow)
        1237 -> drawable(R.drawable.ic_ice)
        1240 -> drawable(R.drawable.ic_rain)
        1243 -> drawable(R.drawable.ic_rain)
        1246 -> drawable(R.drawable.ic_rain)
        1249 -> drawable(R.drawable.ic_ice_rain)
        1252 -> drawable(R.drawable.ic_ice_rain)
        1255 -> drawable(R.drawable.ic_ice_rain)
        1258 -> drawable(R.drawable.ic_ice_rain)
        1261 -> drawable(R.drawable.ic_ice_rain)
        1264 -> drawable(R.drawable.ic_ice_rain)
        1273 -> drawable(R.drawable.ic_thunderstorm)
        1276 -> drawable(R.drawable.ic_thunderstorm)
        1279 -> drawable(R.drawable.ic_thunderstorm)
        1282 -> drawable(R.drawable.ic_thunderstorm)
        else -> drawable(R.drawable.ic_sunny)
    }
}

fun setDayIcon(code: Int): Int {
    return when (code) {
        1000 -> R.drawable.ic_sunny
        1003 -> R.drawable.ic_few_clouds
        1006 -> R.drawable.ic_clouds
        1009 -> R.drawable.ic_mostly_clouds_day
        1030 -> R.drawable.ic_mist
        1063 -> R.drawable.ic_rain
        1066 -> R.drawable.ic_snow
        1069 -> R.drawable.ic_sleet
        1072 -> R.drawable.ic_drizzle
        1087 -> R.drawable.ic_thunderstorm
        1114 -> R.drawable.ic_snow
        1117 -> R.drawable.ic_blizzard
        1135 -> R.drawable.ic_mist
        1147 -> R.drawable.ic_mist
        1150 -> R.drawable.ic_rain
        1153 -> R.drawable.ic_drizzle
        1168 -> R.drawable.ic_drizzle
        1171 -> R.drawable.ic_drizzle
        1180 -> R.drawable.ic_rain
        1183 -> R.drawable.ic_rain
        1186 -> R.drawable.ic_rain
        1189 -> R.drawable.ic_rain
        1192 -> R.drawable.ic_rain
        1195 -> R.drawable.ic_rain
        1198 -> R.drawable.ic_rain
        1201 -> R.drawable.ic_rain
        1204 -> R.drawable.ic_sleet
        1207 -> R.drawable.ic_sleet
        1210 -> R.drawable.ic_snow
        1213 -> R.drawable.ic_snow
        1216 -> R.drawable.ic_snow
        1219 -> R.drawable.ic_snow
        1222 -> R.drawable.ic_snow
        1225 -> R.drawable.ic_snow
        1237 -> R.drawable.ic_ice
        1240 -> R.drawable.ic_rain
        1243 -> R.drawable.ic_rain
        1246 -> R.drawable.ic_rain
        1249 -> R.drawable.ic_ice_rain
        1252 -> R.drawable.ic_ice_rain
        1255 -> R.drawable.ic_ice_rain
        1258 -> R.drawable.ic_ice_rain
        1261 -> R.drawable.ic_ice_rain
        1264 -> R.drawable.ic_ice_rain
        1273 -> R.drawable.ic_thunderstorm
        1276 -> R.drawable.ic_thunderstorm
        1279 -> R.drawable.ic_thunderstorm
        1282 -> R.drawable.ic_thunderstorm
        else -> R.drawable.ic_sunny
    }
}

fun setNightIcon(code: Int): Int {
    return when (code) {
        1000 -> R.drawable.ic_clear_sky
        1003 -> R.drawable.ic_few_clouds_night
        1006 -> R.drawable.ic_clouds
        1009 -> R.drawable.ic_mostly_clouds_night
        1030 -> R.drawable.ic_mist
        1063 -> R.drawable.ic_rain
        1066 -> R.drawable.ic_snow
        1069 -> R.drawable.ic_sleet
        1072 -> R.drawable.ic_drizzle
        1087 -> R.drawable.ic_thunderstorm
        1114 -> R.drawable.ic_snow
        1117 -> R.drawable.ic_blizzard
        1135 -> R.drawable.ic_mist
        1147 -> R.drawable.ic_mist
        1150 -> R.drawable.ic_rain
        1153 -> R.drawable.ic_drizzle
        1168 -> R.drawable.ic_drizzle
        1171 -> R.drawable.ic_drizzle
        1180 -> R.drawable.ic_rain
        1183 -> R.drawable.ic_rain
        1186 -> R.drawable.ic_rain
        1189 -> R.drawable.ic_rain
        1192 -> R.drawable.ic_rain
        1195 -> R.drawable.ic_rain
        1198 -> R.drawable.ic_rain
        1201 -> R.drawable.ic_rain
        1204 -> R.drawable.ic_sleet
        1207 -> R.drawable.ic_sleet
        1210 -> R.drawable.ic_snow
        1213 -> R.drawable.ic_snow
        1216 -> R.drawable.ic_snow
        1219 -> R.drawable.ic_snow
        1222 -> R.drawable.ic_snow
        1225 -> R.drawable.ic_snow
        1237 -> R.drawable.ic_ice
        1240 -> R.drawable.ic_rain
        1243 -> R.drawable.ic_rain
        1246 -> R.drawable.ic_rain
        1249 -> R.drawable.ic_ice_rain
        1252 -> R.drawable.ic_ice_rain
        1255 -> R.drawable.ic_ice_rain
        1258 -> R.drawable.ic_ice_rain
        1261 -> R.drawable.ic_ice_rain
        1264 -> R.drawable.ic_ice_rain
        1273 -> R.drawable.ic_thunderstorm
        1276 -> R.drawable.ic_thunderstorm
        1279 -> R.drawable.ic_thunderstorm
        1282 -> R.drawable.ic_thunderstorm
        else -> R.drawable.ic_clear_sky
    }
}

fun Context.setIconNight(code: Int): Drawable? {
    return when (code) {
        1000 -> drawable(R.drawable.ic_clear_sky)
        1003 -> drawable(R.drawable.ic_few_clouds_night)
        1006 -> drawable(R.drawable.ic_clouds)
        1009 -> drawable(R.drawable.ic_mostly_clouds_night)
        1030 -> drawable(R.drawable.ic_mist)
        1063 -> drawable(R.drawable.ic_rain)
        1066 -> drawable(R.drawable.ic_snow)
        1069 -> drawable(R.drawable.ic_sleet)
        1072 -> drawable(R.drawable.ic_drizzle)
        1087 -> drawable(R.drawable.ic_thunderstorm)
        1114 -> drawable(R.drawable.ic_snow)
        1117 -> drawable(R.drawable.ic_blizzard)
        1135 -> drawable(R.drawable.ic_mist)
        1147 -> drawable(R.drawable.ic_mist)
        1150 -> drawable(R.drawable.ic_rain)
        1153 -> drawable(R.drawable.ic_drizzle)
        1168 -> drawable(R.drawable.ic_drizzle)
        1171 -> drawable(R.drawable.ic_drizzle)
        1180 -> drawable(R.drawable.ic_rain)
        1183 -> drawable(R.drawable.ic_rain)
        1186 -> drawable(R.drawable.ic_rain)
        1189 -> drawable(R.drawable.ic_rain)
        1192 -> drawable(R.drawable.ic_rain)
        1195 -> drawable(R.drawable.ic_rain)
        1198 -> drawable(R.drawable.ic_rain)
        1201 -> drawable(R.drawable.ic_rain)
        1204 -> drawable(R.drawable.ic_sleet)
        1207 -> drawable(R.drawable.ic_sleet)
        1210 -> drawable(R.drawable.ic_snow)
        1213 -> drawable(R.drawable.ic_snow)
        1216 -> drawable(R.drawable.ic_snow)
        1219 -> drawable(R.drawable.ic_snow)
        1222 -> drawable(R.drawable.ic_snow)
        1225 -> drawable(R.drawable.ic_snow)
        1237 -> drawable(R.drawable.ic_ice)
        1240 -> drawable(R.drawable.ic_rain)
        1243 -> drawable(R.drawable.ic_rain)
        1246 -> drawable(R.drawable.ic_rain)
        1249 -> drawable(R.drawable.ic_ice_rain)
        1252 -> drawable(R.drawable.ic_ice_rain)
        1255 -> drawable(R.drawable.ic_ice_rain)
        1258 -> drawable(R.drawable.ic_ice_rain)
        1261 -> drawable(R.drawable.ic_ice_rain)
        1264 -> drawable(R.drawable.ic_ice_rain)
        1273 -> drawable(R.drawable.ic_thunderstorm)
        1276 -> drawable(R.drawable.ic_thunderstorm)
        1279 -> drawable(R.drawable.ic_thunderstorm)
        1282 -> drawable(R.drawable.ic_thunderstorm)
        else -> drawable(R.drawable.ic_clear_sky)
    }
}