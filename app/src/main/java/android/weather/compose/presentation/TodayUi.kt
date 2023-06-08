package android.weather.compose.presentation

import android.weather.compose.Graph
import android.weather.compose.R
import android.weather.compose.models.five.FiveDay
import android.weather.compose.models.forecast.Hour
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.setting.AppSettings
import android.weather.compose.setting.dataStore
import android.weather.compose.ui.theme.BackgroundContainer
import android.weather.compose.ui.theme.BarChartColor
import android.weather.compose.ui.theme.ContainerRow
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.LightText
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.borderColor
import android.weather.compose.ui.theme.containerColor
import android.weather.compose.ui.theme.textColor
import android.weather.compose.utils.CURRENT_FORMAT
import android.weather.compose.utils.HOUR_FORMAT
import android.weather.compose.utils.SUN_FORMAT
import android.weather.compose.utils.Space
import android.weather.compose.utils.TIME_FORMAT
import android.weather.compose.utils.context
import android.weather.compose.utils.defaultZoneId
import android.weather.compose.utils.imageHeight
import android.weather.compose.utils.poppins
import android.weather.compose.utils.poppinsMedium
import android.weather.compose.utils.progressColor
import android.weather.compose.utils.progressFormat
import android.weather.compose.utils.rainSymbol
import android.weather.compose.utils.setAirQualityEmoji
import android.weather.compose.utils.setAirQualityText
import android.weather.compose.utils.setBackgroundDay
import android.weather.compose.utils.setBackgroundImageDay
import android.weather.compose.utils.setBackgroundImageNight
import android.weather.compose.utils.setBackgroundNight
import android.weather.compose.utils.setCurrentWindSpeed
import android.weather.compose.utils.setDayIcon
import android.weather.compose.utils.setNightIcon
import android.weather.compose.utils.setPressure
import android.weather.compose.utils.setRain
import android.weather.compose.utils.setRemainingTime
import android.weather.compose.utils.setTemperature
import android.weather.compose.utils.setVisibility
import android.weather.compose.utils.stringFormat
import android.weather.compose.utils.textBg
import android.weather.compose.utils.timeConverter
import android.weather.compose.utils.windSymbol
import android.weather.compose.utils.windText
import android.weather.compose.view_model.MainViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
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

@Composable
fun TodayUi(viewModel: MainViewModel, navController: NavController) {
    val weather = viewModel.weatherState.collectAsState().value.success
    val fiveDay = viewModel.fiveDayState.success
    val loading = viewModel.weatherState.collectAsState().value.isLoading
    if (loading) LoadingContent()
    val coordinates = viewModel.coordinates.collectAsState(initial = Pair(0.0, 0.0)).value
    weather?.let {
        fiveDay?.let { it1 ->
            SwipeToRefreshToday(
                refresh = loading,
                weather = it, fiveDay = it1, navController = navController,
                onRefresh = {
                    viewModel.currentWeather(coordinates.first, coordinates.second)
                }
            )
        }
    }
}

@Composable
fun TodayUi(weather: WeatherForecast, fiveDay: FiveDay, navController: NavController) {
    val settings = context().dataStore.data.collectAsState(
        initial = AppSettings()
    ).value
    val zone = weather.location?.tz_id ?: defaultZoneId
    val current = weather.current
    val day = weather.forecast?.forecastday?.get(0)?.day
    val code = weather.current?.condition?.code ?: 1000
    val isDay = weather.current?.is_day
    val image = if (isDay == 1) setBackgroundDay(code) else setBackgroundNight(code)
    val backgroundImage =
        if (isDay == 1) setBackgroundImageDay(code) else setBackgroundImageNight(code)
    val tempIcon = if (isDay == 1) setDayIcon(code) else setNightIcon(code)
    val temperature = current?.temp_c?.setTemperature(settings.temperature) ?: "-"
    val dayTemp = day?.maxtemp_c?.setTemperature(settings.temperature) ?: "-"
    val nightTemp = day?.mintemp_c?.setTemperature(settings.temperature) ?: "-"
    val minMaxTemp = stringResource(id = R.string.day_night_temp, dayTemp, nightTemp)
    val conditionText = current?.condition?.text ?: stringResource(id = R.string.sunny)
    val feels = current?.feelslike_c?.setTemperature(settings.temperature) ?: "-"
    val feelsLike = stringResource(id = R.string.real_feel_temp, feels)
    val hour = weather.forecast?.forecastday?.first()?.hour
    val currentTime = weather.location?.localtime_epoch?.timeConverter(CURRENT_FORMAT, zone)
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
            currentTime?.let {
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
            DarkText(text = conditionText,
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
        DetailedUi(
            weather = weather, fiveDay = fiveDay, settings = settings, navController = navController
        )
    }
}

@Composable
fun DetailedUi(
    weather: WeatherForecast,
    fiveDay: FiveDay,
    settings: AppSettings,
    navController: NavController,
) {
    val day = weather.forecast?.forecastday?.get(0)
    val hour = day?.hour
    val volume = weather.forecast?.forecastday?.get(0)?.day?.totalprecip_mm?.setRain(
        settings.precipitation
    ) ?: ""
    CurrentDetails(weather = weather, navController = navController, settings = settings)
    hour?.let {
        Precipitation(
            hour = it,
            volume = volume,
            navController = navController,
            settings = settings
        )
    }
    Wind(weather = weather, navController = navController, settings = settings)
    Sun(weather = weather, fiveDay = fiveDay, navController = navController)
    AirQuality(weather = weather, navController = navController)
}

@Composable
fun CurrentDetails(
    weather: WeatherForecast,
    settings: AppSettings,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val current = weather.current
    val hour = weather.forecast?.forecastday?.first()?.hour
    val pressure = current?.pressure_mb?.setPressure(settings.pressure) ?: ""
    val visibility = current?.vis_km?.setVisibility(settings.visibility) ?: ""
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
                modifier = modifier.fillMaxWidth(0.3f), horizontalAlignment = Alignment.Start
            ) {
                DarkText(text = stringResource(id = R.string.humidity))
                DarkText(text = stringResource(id = R.string.dew_points))
                DarkText(text = stringResource(id = R.string.pressure))
                DarkText(text = stringResource(id = R.string.uv_index))
                DarkText(text = stringResource(id = R.string.visibility))
            }
            Column(modifier = modifier.fillMaxWidth(0.7f)) {
                DarkText(text = "${current?.humidity}%")
                DarkText(text = "${hour?.get(0)?.dewpoint_c}%")
                DarkText(text = pressure)
                DarkText(text = visibility)
                current?.uv?.toString()?.let { DarkText(text = it) }
            }
        }
    })
}

@Composable
fun Precipitation(
    hour: ArrayList<Hour>,
    volume: String,
    navController: NavController,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    BackgroundContainer(columnScope = {
        ContainerRow(text = stringResource(id = R.string.precipitation), onClick = {
            navController.navigate(Graph.DAILY)
        })
//        Content padding denotes to padding from start and end of a row or column
        Space(16)
        RainRow(hour, settings = settings)

        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = LightText, fontFamily = poppins)) {
                append(stringResource(id = R.string.total_daily_volume))
            }
            withStyle(style = SpanStyle()) {
                append("  ")
            }
            withStyle(style = SpanStyle(color = textColor(), fontFamily = poppinsMedium)) {
                append(volume)
            }
        }, modifier = modifier.padding(start = 9.3.dp, top = 12.dp, bottom = 12.dp))
    })
}

@Composable
fun RainRow(item: List<Hour>, settings: AppSettings) {
    LazyRow(contentPadding = PaddingValues(start = 9.3.dp, end = 9.3.dp)) {
        items(count = item.size) {
            val hour = item[it]
            Row {
                if (it == 0)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LightText(text = stringResource(id = R.string.chance))
                        LightText(text = stringResource(id = R.string.volume))
                        LightText(text = settings.precipitation.rainSymbol)
                    }
                Column(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val volume =
                        if (hour.precip_mm != 0.0) hour.precip_mm.setRain(settings.precipitation)
                        else "_"
                    DarkText(text = "${hour.chance_of_rain}%")
                    Space()
                    Image(
                        painter = painterResource(id = R.drawable.rain_icon),
                        contentDescription = null
                    )
                    Space()
                    DarkText(text = volume, textAlign = TextAlign.Center)
                    Space()
                    DarkText(text = hour.time_epoch.timeConverter(HOUR_FORMAT))
                }
            }
        }
    }
}

@Composable
fun Wind(
    weather: WeatherForecast,
    navController: NavController,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    val current = weather.current
    val hour = weather.forecast?.forecastday?.first()?.hour
    val wind = current?.wind_kph?.setCurrentWindSpeed(settings.wind) ?: ""
    val speed = current?.wind_kph
    val dir = current?.wind_dir ?: ""
    val windDegree = current?.wind_degree?.toFloat() ?: 180f
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
                        style = SpanStyle()
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
fun Sun(
    weather: WeatherForecast,
    fiveDay: FiveDay,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val sunrise = fiveDay.city.sunrise.toLong()
    val sunset = fiveDay.city.sunset.toLong()
    val time = weather.location?.localtime_epoch?.toLong()
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
                    contentDescription = "sunrise_icon",
                )
                Space()
                DarkText(text = sunrise.timeConverter(TIME_FORMAT), fontSize = 24.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LightText(text = stringResource(id = R.string.sunrise))
                Space()
                Image(
                    painter = painterResource(id = R.drawable.sunset_icon),
                    contentDescription = "sunset_icon",
                )
                Space()
                DarkText(text = sunset.timeConverter(TIME_FORMAT), fontSize = 24.sp)
            }
        }
        val rise = sunrise.timeConverter(SUN_FORMAT)
        val set = sunset.timeConverter(SUN_FORMAT)
        val current = time?.timeConverter(SUN_FORMAT)
        val length = current?.let { setRemainingTime(rise, set, it).first }
        val remaining = current?.let { setRemainingTime(rise, set, it).second }
        Column(modifier = modifier.padding(9.3.dp)) {
            Text(text = buildAnnotatedString {
                withStyle(SpanStyle(fontFamily = poppins, color = LightText)) {
                    append(stringResource(id = R.string.length_of_day))
                }
                withStyle(SpanStyle()) {
                    append("  ")
                }
                withStyle(SpanStyle(fontFamily = poppinsMedium, color = textColor())) {
                    append(remaining)
                }
            })
            if (time in sunrise..sunset) Text(text = buildAnnotatedString {
                withStyle(SpanStyle(fontFamily = poppins, color = LightText)) {
                    append(stringResource(id = R.string.remaining_daylight))
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
fun AirQuality(
    weather: WeatherForecast,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val airQuality = weather.current?.air_quality
    val pm25 = weather.current?.air_quality?.pm2_5?.stringFormat() ?: ""
    val pm10 = weather.current?.air_quality?.pm10?.stringFormat() ?: ""
    val co = weather.current?.air_quality?.co?.stringFormat() ?: ""
    val no2 = weather.current?.air_quality?.no2?.stringFormat() ?: ""
    val so2 = weather.current?.air_quality?.so2?.stringFormat() ?: ""
    val o3 = weather.current?.air_quality?.o3?.stringFormat() ?: ""
    val progress = weather.current?.air_quality?.pm2_5?.progressFormat() ?: 0.25
    val textColor = airQuality?.pm2_5?.textBg() ?: Color(0xFF22dc30)
    BackgroundContainer(columnScope = {
        ContainerRow(text = stringResource(id = R.string.air_quality), onClick = {
            navController.navigate(Graph.AIR_QUALITY)
        })
        Row(
            modifier = modifier.padding(top = 12.dp, start = 9.3.dp)
        ) {
            airQuality?.pm2_5?.let { setAirQualityEmoji(it).first }
                ?.let { painterResource(id = it) }
                ?.let {
                    Image(
                        modifier = modifier
                            .background(
                                shape = RoundedCornerShape(12.dp), color = containerColor()
                            )
                            .border(
                                width = 0.7.dp,
                                color = borderColor(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp), painter = it, contentDescription = "air_quality"
                    )
                }
            Column(modifier = modifier.padding(start = 9.3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    weather.current?.air_quality?.pm2_5?.stringFormat()?.let {
                        DarkText(
                            text = it, fontSize = 32.7.sp
                        )
                    }
                    airQuality?.pm2_5?.let { setAirQualityText(it) }?.let {
                        DarkText(
                            text = it,
                            modifier = modifier
                                .padding(start = 9.3.dp, bottom = 8.dp)
                                .background(shape = RoundedCornerShape(6.7.dp), color = textColor)
                                .padding(horizontal = 10.dp, vertical = 2.5.dp)
                        )
                    }
                }
                DarkText(
                    text = stringResource(id = R.string.health_warning_of_emergency_conditions_the_entire_population_is_very_large_and_they_are_generating_loads_loads_of_pollution),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
        Space()
        val configuration = LocalConfiguration.current
        airQuality?.pm2_5?.toInt()?.let {
            CustomProgressBar(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .height(5.3.dp),
                width = configuration.screenWidthDp.dp,
                backgroundColor = Color.Gray,
                foregroundColor = progress.progressColor(),
                it
            )
        }
        Space()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LightText(text = stringResource(id = R.string.good))
            LightText(text = stringResource(id = R.string.hazardous))
        }
        Space()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        ) {
            Column(
                modifier = modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pm2), contentDescription = "ic_pm2"
                )
                LightText(text = pm25)
            }
            Column(
                modifier = modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pm10),
                    contentDescription = "ic_pm10"
                )
                LightText(text = pm10)
            }
            Column(
                modifier = modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_co), contentDescription = "ic_co"
                )
                LightText(text = co)
            }
            Column(
                modifier = modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_no2), contentDescription = "ic_no2"
                )
                LightText(text = no2)
            }
            Column(
                modifier = modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_so2), contentDescription = "ic_so2"
                )
                LightText(text = so2)
            }
            Column(
                modifier = modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_03), contentDescription = "ic_03"
                )
                LightText(text = o3)
            }
        }
    })
}

@Composable
fun CustomProgressBar(
    modifier: Modifier, width: Dp, backgroundColor: Color, foregroundColor: Brush, percent: Int,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .background(foregroundColor)
                .width(width * percent / 100)
        )
    }
}

@Composable
fun HourTemp(hourList: ArrayList<Hour>, settings: AppSettings, modifier: Modifier) {
    LazyRow(
        modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(hourList.size) {
            val hour = hourList[it]
            val isDay = hour.is_day
            val code = hour.condition.code
            val icon = if (isDay == 1) setDayIcon(code) else setNightIcon(code)
            Column(
                Modifier.padding(
                    horizontal = 12.dp,
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val temp = hour.temp_c.setTemperature(settings.temperature)
                Text(text = temp, color = Color.White)
                Space()
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "hourly_icon",
                    modifier
                        .height(24.dp)
                        .width(24.dp)
                )
                Space()
                Text(text = hour.time_epoch.timeConverter(HOUR_FORMAT), color = Color.White)
            }
        }
    }
}

@Composable
fun WindHour(hour: ArrayList<Hour>, settings: AppSettings) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(hour.size) {
            val time = hour[it].time_epoch.timeConverter(HOUR_FORMAT)
            val windSpeed = ((hour[it].wind_kph).div(20f))
            val speed = hour[it].wind_kph.setCurrentWindSpeed(settings.wind)
            Chart(data = Triple(speed, windSpeed.toFloat(), time))
        }
    }
}

@Composable
fun Chart(data: Triple<String, Float?, String?>) {
    Column(
        modifier = Modifier
            .padding(9.3.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DarkText(text = data.first, textAlign = TextAlign.Center)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            data.second?.let {
                Modifier
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .width(36.dp)
                    .fillMaxHeight(it)
                    .background(BarChartColor)
            }?.let {
                Box(
                    modifier = it
                )
            }
        }
        Space()
        data.third?.let {
            DarkText(text = it, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun SwipeToRefreshToday(
    refresh: Boolean,
    weather: WeatherForecast,
    fiveDay: FiveDay,
    navController: NavController,
    onRefresh: () -> Unit,
) {
    val state = rememberSwipeRefreshState(isRefreshing = refresh)
    SwipeRefresh(state = state, onRefresh = { onRefresh() }) {
        TodayUi(weather = weather, fiveDay = fiveDay, navController = navController)
    }
}