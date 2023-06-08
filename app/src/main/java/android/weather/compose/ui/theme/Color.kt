package android.weather.compose.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val SplashLightColor = Color(0xFF46b7de)
val SplashDarkColor = Color(0xFF0f1441)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val AppColor = Color(0xFF0A7BD1)
val BackgroundLight = Color(0xFFf3f4f6)
val BackgroundDark = Color(0xFF1b1c1e)
val DayColor = Color(0xFF43b8de)
val CloudyColor = Color(0xFFb9cff1)
val WinterColor = Color(0xFF0d54b9)
val HazeColor = Color(0xFF808285)
val NightColor = Color(0xFF0d356a)
val CloudyNightColor = Color(0xFF000610)
val WinterNightColor = Color(0xFF000610)
val LightContainer = Color(0xFFffffff)
val DarkContainer = Color(0xFF27282d)
val LightText = Color(0xFF89909a)
val BarChartColor = Color(0xFF049ae6)
val FirstColor = Color(0xFF22dc30)
val SecColor = Color(0xFFffc400)
val ThirdColor = Color(0xFFf63838)
val FourthColor = Color(0xFF854df1)
val FifthColor = Color(0xFF591717)

@SuppressLint("InvalidColorHexValue")
val PopupBg = Color(0xFF0D049AE6)

@Composable
fun splashColor() = if (isSystemInDarkTheme()) SplashDarkColor else SplashLightColor

@Composable
fun containerColor() = if (isSystemInDarkTheme()) DarkContainer else LightContainer

@Composable
fun backgroundColor() = if (isSystemInDarkTheme()) BackgroundDark else BackgroundLight

@Composable
fun borderColor() = if (isSystemInDarkTheme()) Color(0xFF2c2c2c) else Color(0xFFdcdcdc)

@Composable
fun dividerColor() = if (isSystemInDarkTheme()) Color(0xFF373737) else Color(0xFFdcdcdc)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)