package android.weather.compose.presentation

import android.weather.compose.R
import android.weather.compose.models.five.Day
import android.weather.compose.models.five.FiveDay
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.setting.AppSettings
import android.weather.compose.setting.dataStore
import android.weather.compose.ui.theme.BackgroundContainer
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.LightText
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.dividerColor
import android.weather.compose.utils.DAY_FORMAT
import android.weather.compose.utils.HOUR_FORMAT
import android.weather.compose.utils.REGION_FORMAT
import android.weather.compose.utils.Space
import android.weather.compose.utils.context
import android.weather.compose.utils.defaultZoneId
import android.weather.compose.utils.getNextDays
import android.weather.compose.utils.setDayIcon
import android.weather.compose.utils.setIconWidget
import android.weather.compose.utils.setTemperature
import android.weather.compose.utils.setVisibility
import android.weather.compose.utils.setWindSpeed
import android.weather.compose.utils.timeConverter
import android.weather.compose.view_model.MainViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MainFiveDayUi(viewModel: MainViewModel) {
    val loading = viewModel.weatherState.collectAsState().value.isLoading
    val coordinates = viewModel.coordinates.collectAsState(initial = Pair(0.0, 0.0)).value
    if (loading) LoadingContent()
    viewModel.weatherState.collectAsState().value.success?.let {
        viewModel.fiveDayState.success?.let { five ->
            SwipeToRefreshFiveDay(refresh = loading, weather = it, fiveDay = five) {
                viewModel.fiveDayWeather(coordinates.first, coordinates.second)
            }
        }
    }
}

@Composable
fun FiveDayUi(weather: WeatherForecast, fiveDay: FiveDay) {
    val settings = context().dataStore.data.collectAsState(
        initial = AppSettings()
    ).value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor()), contentAlignment = Alignment.TopStart
    ) {
        BackgroundContainer(
            modifier = Modifier
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            FirstDay(
                weather = weather,
                fiveDay = fiveDay,
                settings = settings
            )
            Divide()
            SecDay(
                weather = weather,
                fiveDay = fiveDay,
                settings = settings
            )
            Divide()
            ThirdDay(
                weather = weather,
                fiveDay = fiveDay,
                settings = settings
            )
            Divide()
            FourthDay(
                weather = weather,
                fiveDay = fiveDay,
                settings = settings
            )
            Divide()
            FifthDay(
                weather = weather,
                fiveDay = fiveDay,
                settings = settings
            )
        }
    }
}

@Composable
fun FirstDay(
    weather: WeatherForecast,
    fiveDay: FiveDay,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    val zone = weather.location?.tz_id ?: defaultZoneId
    val day = weather.forecast?.forecastday?.get(0)
    val min = day?.day?.mintemp_c?.setTemperature(settings.temperature)
    val max = day?.day?.maxtemp_c?.setTemperature(settings.temperature)
    val code = day?.day?.condition?.code ?: 1000
    val icon = setDayIcon(code)
    val first = fiveDay.list.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(getNextDays(1, zone, REGION_FORMAT)[0])
    }
    val currentDay = first.first().dt.timeConverter(DAY_FORMAT, zone)
    val wind = fiveDay.list.first().wind.speed.setWindSpeed(settings.wind)
    val humidity = "${fiveDay.list.first().main.humidity}%"
    val visibility = fiveDay.list.first().visibility.setVisibility(settings.visibility)
    var displayInfo by remember {
        mutableStateOf(false)
    }
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { displayInfo = !displayInfo }
                .padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                DarkText(text = currentDay)
                day?.day?.condition?.text?.let { LightText(text = it) }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = icon),
                    modifier = modifier
                        .padding(end = 18.dp)
                        .size(42.7.dp),
                    contentDescription = "first_day_icon",
                )
                Column {
                    max?.let { DarkText(text = it) }
                    min?.let { LightText(text = it) }
                }
            }
        }
        AnimatedVisibility(visible = displayInfo) {
            Column(modifier = modifier.fillMaxWidth()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        LightText(text = stringResource(id = R.string.wind))
                        LightText(text = stringResource(id = R.string.humidity))
                        LightText(text = stringResource(id = R.string.visibility))
                    }
                    Column {
                        DarkText(text = wind)
                        DarkText(text = humidity)
                        DarkText(text = visibility)
                    }
                }
                DetailedList(list = first, modifier = modifier, settings = settings, zone = zone)
            }
        }
    }
}

@Composable
fun SecDay(
    weather: WeatherForecast,
    fiveDay: FiveDay,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    val zone = weather.location?.tz_id ?: defaultZoneId
    val day = weather.forecast?.forecastday?.get(1)
    val min = day?.day?.mintemp_c?.setTemperature(settings.temperature)
    val max = day?.day?.maxtemp_c?.setTemperature(settings.temperature)
    val code = day?.day?.condition?.code ?: 1000
    val icon = setDayIcon(code)
    val second = fiveDay.list.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(getNextDays(2, zone, REGION_FORMAT)[1])
    }
    val currentDay = second.first().dt.timeConverter(DAY_FORMAT, zone)
    val wind = fiveDay.list.first().wind.speed.setWindSpeed(settings.wind)
    val humidity = "${fiveDay.list.first().main.humidity}%"
    val visibility = fiveDay.list.first().visibility.setVisibility(settings.visibility)
    var displayInfo by remember {
        mutableStateOf(false)
    }
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { displayInfo = !displayInfo }
                .padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                DarkText(text = currentDay)
                day?.day?.condition?.text?.let { LightText(text = it) }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = icon),
                    modifier = modifier
                        .padding(end = 18.dp)
                        .size(42.7.dp),
                    contentDescription = "first_day_icon",
                )
                Column {
                    max?.let { DarkText(text = it) }
                    min?.let { LightText(text = it) }
                }
            }
        }
        AnimatedVisibility(visible = displayInfo) {
            Column(modifier = modifier.fillMaxWidth()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        LightText(text = stringResource(id = R.string.wind))
                        LightText(text = stringResource(id = R.string.humidity))
                        LightText(text = stringResource(id = R.string.visibility))
                    }
                    Column {
                        DarkText(text = wind)
                        DarkText(text = humidity)
                        DarkText(text = visibility)
                    }
                }
                DetailedList(list = second, modifier = modifier, settings = settings, zone = zone)
            }
        }
    }
}

@Composable
fun ThirdDay(
    weather: WeatherForecast,
    fiveDay: FiveDay,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    val zone = weather.location?.tz_id ?: defaultZoneId
    val day = weather.forecast?.forecastday?.get(2)
    val min = day?.day?.mintemp_c?.setTemperature(settings.temperature)
    val max = day?.day?.maxtemp_c?.setTemperature(settings.temperature)
    val code = day?.day?.condition?.code ?: 1000
    val icon = setDayIcon(code)
    val third = fiveDay.list.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(getNextDays(3, zone, REGION_FORMAT)[2])
    }
    val currentDay = third.first().dt.timeConverter(DAY_FORMAT, zone)
    val wind = fiveDay.list.first().wind.speed.setWindSpeed(settings.wind)
    val humidity = "${fiveDay.list.first().main.humidity}%"
    val visibility = fiveDay.list.first().visibility.setVisibility(settings.visibility)
    var displayInfo by remember {
        mutableStateOf(false)
    }
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { displayInfo = !displayInfo }
                .padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                DarkText(text = currentDay)
                day?.day?.condition?.text?.let { LightText(text = it) }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = icon),
                    modifier = modifier
                        .padding(end = 18.dp)
                        .size(42.7.dp),
                    contentDescription = "first_day_icon",
                )
                Column {
                    max?.let { DarkText(text = it) }
                    min?.let { LightText(text = it) }
                }
            }
        }
        AnimatedVisibility(visible = displayInfo) {
            Column(modifier = modifier.fillMaxWidth()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        LightText(text = stringResource(id = R.string.wind))
                        LightText(text = stringResource(id = R.string.humidity))
                        LightText(text = stringResource(id = R.string.visibility))
                    }
                    Column {
                        DarkText(text = wind)
                        DarkText(text = humidity)
                        DarkText(text = visibility)
                    }
                }
                DetailedList(list = third, modifier = modifier, settings = settings, zone = zone)
            }
        }
    }
}

@Composable
fun FourthDay(
    weather: WeatherForecast,
    fiveDay: FiveDay,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    val zone = weather.location?.tz_id ?: defaultZoneId
    val fourth = fiveDay.list.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(getNextDays(4, zone, REGION_FORMAT)[3])
    }
    val min = fourth[4].main.temp_min.setTemperature(settings.temperature)
    val max = fourth[4].main.temp_max.setTemperature(settings.temperature)
    val icon = fourth[4].weather.first().icon.let {
        setIconWidget(it)
    }
    val condition = fourth[4].weather.first().description
    val currentDay = fourth.first().dt.timeConverter(DAY_FORMAT, zone)
    val wind = fiveDay.list.first().wind.speed.setWindSpeed(settings.wind)
    val humidity = "${fiveDay.list.first().main.humidity}%"
    val visibility = fiveDay.list.first().visibility.setVisibility(settings.visibility)
    var displayInfo by remember {
        mutableStateOf(false)
    }
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { displayInfo = !displayInfo }
                .padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                DarkText(text = currentDay)
                LightText(text = condition)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = icon),
                    modifier = modifier
                        .padding(end = 18.dp)
                        .size(42.7.dp),
                    contentDescription = "first_day_icon",
                )
                Column {
                    DarkText(text = max)
                    LightText(text = min)
                }
            }
        }
        AnimatedVisibility(visible = displayInfo) {
            Column(modifier = modifier.fillMaxWidth()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        LightText(text = stringResource(id = R.string.wind))
                        LightText(text = stringResource(id = R.string.humidity))
                        LightText(text = stringResource(id = R.string.visibility))
                    }
                    Column {
                        DarkText(text = wind)
                        DarkText(text = humidity)
                        DarkText(text = visibility)
                    }
                }
                DetailedList(list = fourth, modifier = modifier, settings = settings, zone = zone)
            }
        }
    }
}

@Composable
fun FifthDay(
    weather: WeatherForecast,
    fiveDay: FiveDay,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    val zone = weather.location?.tz_id ?: defaultZoneId
    val fifth = fiveDay.list.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(getNextDays(5, zone, REGION_FORMAT)[4])
    }
    val min = fifth[4].main.temp_min.setTemperature(settings.temperature)
    val max = fifth[4].main.temp_max.setTemperature(settings.temperature)
    val icon = fifth[4].weather.first().icon.let {
        setIconWidget(it)
    }
    val condition = fifth[4].weather.first().description
    val currentDay = fifth.first().dt.timeConverter(DAY_FORMAT, zone)
    val wind = fiveDay.list.first().wind.speed.setWindSpeed(settings.wind)
    val humidity = "${fiveDay.list.first().main.humidity}%"
    val visibility = fiveDay.list.first().visibility.setVisibility(settings.visibility)
    var displayInfo by remember {
        mutableStateOf(false)
    }
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { displayInfo = !displayInfo }
                .padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                DarkText(text = currentDay)
                LightText(text = condition)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = icon),
                    modifier = modifier
                        .padding(end = 18.dp)
                        .size(42.7.dp),
                    contentDescription = "first_day_icon",
                )
                Column {
                    DarkText(text = max)
                    LightText(text = min)
                }
            }
        }
        AnimatedVisibility(visible = displayInfo) {
            Column(modifier = modifier.fillMaxWidth()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        LightText(text = stringResource(id = R.string.wind))
                        LightText(text = stringResource(id = R.string.humidity))
                        LightText(text = stringResource(id = R.string.visibility))
                    }
                    Column {
                        DarkText(text = wind)
                        DarkText(text = humidity)
                        DarkText(text = visibility)
                    }
                }
                DetailedList(list = fifth, modifier = modifier, settings = settings, zone = zone)
            }
        }
    }
}

@Composable
fun DetailedList(list: List<Day>, modifier: Modifier, settings: AppSettings, zone: String) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 10.dp),
        modifier = modifier.padding(vertical = 18.dp)
    ) {
        items(list.size) {
            val item = list[it]
            val image = item.weather.first().icon.let { icon ->
                setIconWidget(
                    icon
                )
            }
            Column(
                modifier = modifier.padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DarkText(text = item.main.temp.setTemperature(settings.temperature))
                Space()
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "detailed_icon",
                    modifier = Modifier.size(27.dp)
                )
                Space()
                DarkText(text = item.dt.timeConverter(HOUR_FORMAT, zone))
            }
        }
    }
}

@Composable
fun SwipeToRefreshFiveDay(
    refresh: Boolean,
    weather: WeatherForecast,
    fiveDay: FiveDay,
    onRefresh: () -> Unit,
) {
    val state = rememberSwipeRefreshState(isRefreshing = refresh)
    SwipeRefresh(state = state, onRefresh = { onRefresh() }) {
        FiveDayUi(weather = weather, fiveDay = fiveDay)
    }
}

@Composable
fun Divide() {
    Divider(
        color = dividerColor(), modifier = Modifier
            .fillMaxWidth()
            .height(0.3.dp)
    )
}