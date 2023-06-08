package android.weather.compose.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.weather.compose.R
import android.weather.compose.ui.theme.FifthColor
import android.weather.compose.ui.theme.FirstColor
import android.weather.compose.ui.theme.FourthColor
import android.weather.compose.ui.theme.SecColor
import android.weather.compose.ui.theme.ThirdColor
import android.weather.compose.view_model.MainViewModel
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

const val CHANNEL_ID = "CHANNEL_ID"
const val LAST_LANGUAGE = "LAST_LANGUAGE"
const val CURRENT_LON = "CURRENT_LON"
const val CURRENT_LAT = "CURRENT_LAT"
val defaultZoneId: String = TimeZone.getDefault().id

fun getNextDays(days: Int, id: String, format: String): ArrayList<String> {
    val list = ArrayList<String>()
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    for (i in 0 until days) {
        val calendar: Calendar = GregorianCalendar()
        calendar.add(Calendar.DATE, i)
        sdf.timeZone = TimeZone.getTimeZone(id)
        val day: String = sdf.format(calendar.time)
        list.add(day)
    }
    return list
}

fun calculateDifference(startDate: Date, endDate: Date): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    var difference = endDate.time - startDate.time
    if (difference < 0) {
        val dateMax = simpleDateFormat.parse("24:00")
        val dateMin = simpleDateFormat.parse("00:00")
        if (dateMax != null && dateMin != null) {
            difference = dateMax.time - startDate.time + (endDate.time - dateMin.time)
        }
    }
    val days = (difference / (1000 * 60 * 60 * 24)).toInt()
    val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
    val min =
        (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
    return "${hours}h ${min}m"
}

fun setRemainingTime(rise: String, set: String, current: String): Pair<String, String> {
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val riseTime = simpleDateFormat.parse(rise) as Date
    val setTime = simpleDateFormat.parse(set) as Date
    val currentTime = simpleDateFormat.parse(current) as Date
    val dayLengthDiff = calculateDifference(riseTime, setTime)
    val remainingTime = calculateDifference(currentTime, setTime)
    return Pair(remainingTime, dayLengthDiff)
}

fun Context.onShareAppClick() {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TITLE, "Hey check out this app")
        putExtra(Intent.EXTRA_SUBJECT, "Hey check out this amazing app")
        putExtra(
            Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=$packageName"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Context.vectorToBitmap(@DrawableRes drawableId: Int): Bitmap? {
    val drawable = drawable(drawableId) ?: return null
    val bitmap = createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Context.dpToPx(dp: Float) = (dp * (resources.displayMetrics.densityDpi / 160f)).toInt()

inline fun <reified T> Activity.gotoActivity(crossinline action: Intent.() -> Unit = {}) {
    Intent(this, T::class.java).apply {
        action(this)
        startActivity(this)
    }
}

@Composable
fun imageHeight(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp.dp - 104.dp
}

val poppins = FontFamily(fonts = listOf(Font(resId = R.font.poppins_regular)))
val poppinsMedium = FontFamily(fonts = listOf(Font(resId = R.font.poppins_medium)))

@Composable
fun isDay(viewModel: MainViewModel): Int {
    val weather = viewModel.weatherState.collectAsState().value.success
    return weather?.current?.is_day ?: 1
}

@Composable
fun currentCode(viewModel: MainViewModel): Int {
    val weather = viewModel.weatherState.collectAsState().value.success
    return weather?.current?.condition?.code ?: 1000
}

@Composable
fun tomorrowCode(viewModel: MainViewModel): Int {
    val weather = viewModel.weatherState.collectAsState().value.success
    return weather?.forecast?.forecastday?.get(1)?.day?.condition?.code ?: 1000
}

@Composable
fun Space(height: Number = 8.0, width: Number = 0) {
    Spacer(
        modifier = Modifier
            .height(height.toDouble().dp)
            .width(width.toDouble().dp)
    )
}

@Composable
fun context() = LocalContext.current

val Double.windText
    get() = when {
        this < 1.0 -> "Calm"
        this in 1.0..5.0 -> "Light Air"
        this in 6.0..11.0 -> "Light Breeze"
        this in 12.0..19.0 -> "Gentle Breeze"
        this in 20.0..28.0 -> "Moderate Breeze"
        this in 29.0..38.0 -> "Fresh Breeze"
        this in 39.0..49.0 -> "Strong gale"
        this in 50.0..61.0 -> "Fresh Breeze"
        this in 62.0..74.0 -> "Fresh Breeze"
        this in 75.0..88.0 -> "Strong gale"
        this in 89.0..102.0 -> "Whole gale"
        this in 103.0..117.0 -> "Storm"
        this > 117.0 -> "Hurricane"
        else -> "Light Breeze"
    }

fun setAirQualityEmoji(airQuality: Double): Pair<Int, Int> {
    return when {
        airQuality in 0.0..50.0 -> Pair(
            R.drawable.ic_wao_emoji, R.drawable.first_air_quality_progress
        )

        airQuality in 51.0..100.0 -> Pair(
            R.drawable.ic_happy_emoji, R.drawable.second_air_quality_progress
        )

        airQuality in 101.0..150.0 -> Pair(
            R.drawable.ic_cry_emoji, R.drawable.third_air_quality_progress
        )

        airQuality in 151.0..200.0 -> Pair(
            R.drawable.ic_weep_emoji, R.drawable.air_quality_progress_secondary
        )

        airQuality >= 201 -> Pair(
            R.drawable.ic_hazardous, R.drawable.air_quality_progress_secondary
        )

        else -> Pair(
            R.drawable.ic_happy_emoji, R.drawable.third_air_quality_progress
        )
    }
}

@Composable
fun setAirQualityText(airQuality: Double) = when {
    (airQuality >= 0) and (airQuality <= 50) -> stringResource(R.string.good)
    (airQuality >= 51) and (airQuality <= 100) -> stringResource(R.string.moderate)
    (airQuality >= 101) and (airQuality <= 150) -> stringResource(R.string.unhealthy)
    (airQuality >= 151) and (airQuality <= 200) -> stringResource(R.string.very_unhealthy)
    (airQuality >= 201) -> stringResource(R.string.hazardous)
    else -> stringResource(R.string.good)
}

@Composable
fun Double.progressColor() = when {
    this in 0.0..0.50 -> Brush.horizontalGradient(listOf(FirstColor, Color.Transparent))
    this in 0.51..0.99 -> Brush.horizontalGradient(listOf(FirstColor, SecColor))
    this in 0.100..0.149 -> Brush.horizontalGradient(listOf(FirstColor, SecColor, ThirdColor))
    this in 0.150..0.200 -> Brush.horizontalGradient(
        listOf(
            FirstColor, SecColor, ThirdColor, FourthColor
        )
    )

    this >= 0.201 -> Brush.horizontalGradient(
        listOf(
            FirstColor, SecColor, ThirdColor, FourthColor, FifthColor
        )
    )

    else -> Brush.horizontalGradient(listOf(FirstColor, SecColor, ThirdColor))
}

@Composable
fun Double.textBg() = when {
    this in 0.0..50.0 -> Color(0xFF22dc30)
    this in 51.0..100.0 -> Color(0xFFffc400)
    this in 101.0..150.0 -> Color(0xFFf63838)
    this in 150.0..200.0 -> Color(0xFF854df1)
    this >= 201.0 -> Color(0xFF591717)
    else -> Color(0xFF22dc30)
}