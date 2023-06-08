package android.weather.compose.presentation

import android.weather.compose.Graph
import android.weather.compose.R
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.setting.AppSettings
import android.weather.compose.setting.dataStore
import android.weather.compose.ui.theme.BackgroundContainer
import android.weather.compose.ui.theme.ContainerRow
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.LightText
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.textColor
import android.weather.compose.utils.SUN_FORMAT
import android.weather.compose.utils.Space
import android.weather.compose.utils.TOMORROW_FORMAT
import android.weather.compose.utils.calculateDifference
import android.weather.compose.utils.context
import android.weather.compose.utils.dateToMillis
import android.weather.compose.utils.defaultZoneId
import android.weather.compose.utils.imageHeight
import android.weather.compose.utils.poppins
import android.weather.compose.utils.poppinsMedium
import android.weather.compose.utils.setBackgroundDay
import android.weather.compose.utils.setBackgroundImageDay
import android.weather.compose.utils.setCurrentWindSpeed
import android.weather.compose.utils.setDayIcon
import android.weather.compose.utils.setRain
import android.weather.compose.utils.setTemperature
import android.weather.compose.utils.setVisibility
import android.weather.compose.utils.sunConverter
import android.weather.compose.utils.timeConverter
import android.weather.compose.utils.windSymbol
import android.weather.compose.utils.windText
import android.weather.compose.view_model.MainViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TomorrowMainUi(viewModel: MainViewModel, navController: NavController) {
    val weather = viewModel.weatherState.collectAsState().value.success
    val loading = viewModel.weatherState.collectAsState().value.isLoading
    val coordinates = viewModel.coordinates.collectAsState(initial = Pair(0.0, 0.0)).value
    if (viewModel.weatherState.collectAsState().value.isLoading)
        LoadingContent()
    weather?.let {
        SwipeToRefreshTomorrow(
            refresh = loading,
            weather = it,
            navController = navController
        ) {
            viewModel.currentWeather(coordinates.first, coordinates.second)
        }
    }
}

@Composable
fun TempUiTomorrow(weather: WeatherForecast, navController: NavController) {
    val zone = weather.location?.tz_id ?: defaultZoneId
    val settings = context().dataStore.data.collectAsState(
        initial = AppSettings()
    ).value
    val day = weather.forecast?.forecastday?.get(1)?.day
    val code = day?.condition?.code ?: 1000
    val image = setBackgroundDay(code)
    val backgroundImage = setBackgroundImageDay(code)
    val tempIcon = setDayIcon(code)
    val temperature = day?.avgtemp_c?.setTemperature(settings.temperature) ?: "-"
    val dayTemp = day?.maxtemp_c?.setTemperature(settings.temperature) ?: "-"
    val nightTemp = day?.mintemp_c?.setTemperature(settings.temperature) ?: "-"
    val minMaxTemp = stringResource(id = R.string.day_night_temp, dayTemp, nightTemp)
    val conditionText = day?.condition?.text
    val feels = day?.avgtemp_c?.setTemperature(settings.temperature) ?: "-"
    val feelsLike = stringResource(id = R.string.real_feel_temp, feels)
    val hour = weather.forecast?.forecastday?.get(1)?.hour
    val tomorrowTime =
        weather.forecast?.forecastday?.get(1)?.date_epoch?.timeConverter(TOMORROW_FORMAT, zone)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor())
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight())
        ) {
            val (
                backgroundIcon, time, dayNightTemp, temp,
                realFeel, icon, condition, graph,
            ) = createRefs()
            AsyncImage(
                model = image,
                contentDescription = "background_color",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            AsyncImage(model = backgroundImage,
                contentDescription = "background_image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(backgroundIcon) {
                        bottom.linkTo(parent.bottom)
                    })
            AsyncImage(model = tempIcon,
                contentDescription = "icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(temp.top)
                        bottom.linkTo(temp.bottom)
                        end.linkTo(parent.end, margin = 32.dp)
                        height = Dimension.fillToConstraints
                    }
                    .aspectRatio(1f))
            tomorrowTime?.let {
                Text(text = it,
                    color = Color.White,
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(parent.top, margin = 15.7.dp)
                        start.linkTo(parent.start, margin = 24.dp)
                    })
            }
            Text(text = minMaxTemp,
                color = Color.White,
                modifier = Modifier.constrainAs(dayNightTemp) {
                    top.linkTo(time.bottom)
                    start.linkTo(parent.start, margin = 24.dp)
                })
            Text(text = temperature,
                color = Color.White,
                fontSize = 66.sp,
                modifier = Modifier.constrainAs(temp) {
                    start.linkTo(parent.start, margin = 24.dp)
                    top.linkTo(dayNightTemp.bottom)
                })
            conditionText?.let {
                DarkText(text = it,
                    color = Color.White,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.constrainAs(condition) {
                        end.linkTo(icon.end)
                        top.linkTo(realFeel.top)
                        start.linkTo(icon.start)
                        width = Dimension.fillToConstraints
                    })
            }
            Text(text = feelsLike, color = Color.White, modifier = Modifier.constrainAs(realFeel) {
                start.linkTo(time.start)
                top.linkTo(temp.bottom, margin = 8.dp)
            })
            hour?.let {
                HourTemp(hourList = it,
                    settings = settings,
                    modifier = Modifier.constrainAs(graph) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            }
        }
        DetailedUiTomorrow(weather = weather, settings = settings, navController = navController)
    }
}

@Composable
fun DetailedUiTomorrow(
    weather: WeatherForecast,
    settings: AppSettings,
    navController: NavController,
) {
    val day = weather.forecast?.forecastday?.get(1)
    val hour = day?.hour
    val volume = weather.forecast?.forecastday?.get(1)?.day?.totalprecip_mm?.setRain(
        settings.precipitation
    ) ?: ""
    CurrentDetailsTomorrow(weather = weather, settings = settings, navController = navController)
    hour?.let {
        Precipitation(
            hour = it,
            volume = volume,
            settings = settings,
            navController = navController
        )
    }
    WindTomorrow(weather = weather, settings = settings, navController = navController)
    SunTomorrow(weather = weather, navController = navController)
}

@Composable
fun CurrentDetailsTomorrow(
    weather: WeatherForecast,
    settings: AppSettings,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val day = weather.forecast?.forecastday?.get(1)?.day
    val visibility = day?.avgvis_km?.setVisibility(settings.visibility) ?: ""
    BackgroundContainer(columnScope = {
        ContainerRow(text = stringResource(id = R.string.current_details), onClick = {
            navController.navigate(Graph.DAILY)
        })
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 9.3.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.weight(0.65f), horizontalAlignment = Alignment.Start
            ) {
                DarkText(text = stringResource(id = R.string.humidity))
                DarkText(text = stringResource(id = R.string.uv_index))
                DarkText(text = stringResource(id = R.string.visibility))
            }
            Column(modifier = modifier.weight(0.5f)) {
                DarkText(text = "${day?.avghumidity}%")
                day?.uv?.toString()?.let { DarkText(text = it) }
                DarkText(text = visibility)
            }
        }
    })
}

@Composable
fun WindTomorrow(
    weather: WeatherForecast,
    settings: AppSettings,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val day = weather.forecast?.forecastday?.get(1)?.day
    val hour = weather.forecast?.forecastday?.get(1)?.hour
    val wind = day?.maxwind_kph?.setCurrentWindSpeed(settings.wind) ?: ""
    val speed = day?.maxwind_kph
    val dir = hour?.get(12)?.wind_dir ?: ""
    val windDegree = hour?.get(12)?.wind_degree?.toFloat()?.plus(180f) ?: 180f
    BackgroundContainer(columnScope = {
        ContainerRow(text = stringResource(id = R.string.wind), onClick = {
            navController.navigate(Graph.DAILY)
        })
        val anim by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wind_animation))
        Row(modifier = modifier.padding(9.3.dp)) {
            Column(modifier = modifier) {
                Row(modifier) {
                    DarkText(text = wind, fontSize = 52.sp)
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.ic_navigation),
                            contentDescription = "wind_direction",
                            modifier = modifier
                                .rotate(windDegree)
                                .padding(top = 13.dp)
                        )
                        DarkText(text = settings.wind.windSymbol)
                    }
                }
                speed?.windText?.let { DarkText(text = it, fontSize = 24.7.sp) }
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins
                        )
                    ) {
                        append(stringResource(id = R.string.now))
                    }
                    withStyle(style = SpanStyle()) {
                        append(" ")
                    }
                    withStyle(
                        style = SpanStyle(

                        )
                    ) {
                        append(stringResource(id = R.string.wind_dir, dir))
                    }
                })
            }
            LottieAnimation(composition = anim, iterations = LottieConstants.IterateForever)
        }
        hour?.let { WindHour(hour = it, settings = settings) }
    })
}

@Composable
fun SunTomorrow(
    weather: WeatherForecast,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val zone = weather.location?.tz_id ?: defaultZoneId
    val day = weather.forecast?.forecastday?.get(1)
    val sunrise =
        weather.forecast?.forecastday?.get(1)?.astro?.sunrise?.replace("AM", "am")
    val sunset =
        weather.forecast?.forecastday?.get(1)?.astro?.sunset?.replace("PM", "pm")
    BackgroundContainer(columnScope = {
        ContainerRow(text = stringResource(id = R.string.sunrise_amp_sunset), onClick = {
            navController.navigate(Graph.DAILY)
        })
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LightText(text = stringResource(id = R.string.sunrise))
                Space()
                Image(
                    painter = painterResource(id = R.drawable.sunrise_icon),
                    contentDescription = "sunrise_icon"
                )
                Space()
                sunrise?.let { DarkText(text = it, fontSize = 24.sp) }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LightText(text = stringResource(id = R.string.sunrise))
                Space()
                Image(
                    painter = painterResource(id = R.drawable.sunset_icon),
                    contentDescription = "sunset_icon"
                )
                Space()
                sunset?.let { DarkText(text = it, fontSize = 24.sp) }
            }
        }
        val sunriseTomorrow =
            day?.astro?.sunrise?.dateToMillis(zone)?.sunConverter(SUN_FORMAT, zone)
        val sunsetTomorrow = day?.astro?.sunset?.dateToMillis(zone)?.sunConverter(SUN_FORMAT, zone)
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val riseTime = sunriseTomorrow?.let { simpleDateFormat.parse(it) } as Date
        val setTime = sunsetTomorrow?.let { simpleDateFormat.parse(it) } as Date
        val length = calculateDifference(riseTime, setTime)
        Column(modifier = modifier.padding(9.3.dp)) {
            Text(text = buildAnnotatedString {
                withStyle(SpanStyle(fontFamily = poppins, color = LightText)) {
                    append(stringResource(id = R.string.length_of_day))
                }
                withStyle(SpanStyle()) {
                    append("  ")
                }
                withStyle(SpanStyle(fontFamily = poppinsMedium, color = textColor())) {
                    append(length)
                }
            })
        }
    })
}

@Composable
fun SwipeToRefreshTomorrow(
    refresh: Boolean,
    weather: WeatherForecast,
    navController: NavController,
    onRefresh: () -> Unit,
) {
    val state = rememberSwipeRefreshState(isRefreshing = refresh)
    SwipeRefresh(state = state, onRefresh = { onRefresh() }) {
        TempUiTomorrow(weather = weather, navController = navController)
    }
}