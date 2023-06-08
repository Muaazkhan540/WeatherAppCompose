@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)

package android.weather.compose.presentation

import android.weather.compose.R
import android.weather.compose.models.five.Day
import android.weather.compose.setting.AppSettings
import android.weather.compose.setting.dataStore
import android.weather.compose.ui.theme.BackgroundContainer
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.LightText
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.containerColor
import android.weather.compose.ui.theme.textColor
import android.weather.compose.utils.DAILY_DETAIL_FORMAT
import android.weather.compose.utils.HOUR_FORMAT
import android.weather.compose.utils.REGION_FORMAT
import android.weather.compose.utils.context
import android.weather.compose.utils.defaultZoneId
import android.weather.compose.utils.getNextDays
import android.weather.compose.utils.setIconWidget
import android.weather.compose.utils.setPressure
import android.weather.compose.utils.setTemperature
import android.weather.compose.utils.setVisibility
import android.weather.compose.utils.setWindSpeed
import android.weather.compose.utils.timeConverter
import android.weather.compose.view_model.MainViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(navController: NavController, viewModel: MainViewModel) {
    Scaffold(
        containerColor = backgroundColor(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor()),
                title = {
                    DarkText(
                        text = stringResource(id = R.string.daily_forecast),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            DetailedMainUi(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}

@Composable
fun DetailedMainUi(viewModel: MainViewModel, modifier: Modifier) {
    rememberSystemUiController().apply {
        setStatusBarColor(color = containerColor())
    }
    val settings = context().dataStore.data.collectAsState(
        initial = AppSettings()
    ).value
    val fiveDay = viewModel.fiveDayState.success
    val weather = viewModel.weatherState.collectAsState().value.success
    val zone = weather?.location?.tz_id ?: defaultZoneId
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val days = getNextDays(6, zone, DAILY_DETAIL_FORMAT)
    val first = fiveDay?.list?.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(
            getNextDays(1, zone, REGION_FORMAT)[0]
        )
    }
    val second = fiveDay?.list?.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(
            getNextDays(2, zone, REGION_FORMAT)[1]
        )
    }
    val third = fiveDay?.list?.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(
            getNextDays(3, zone, REGION_FORMAT)[2]
        )
    }
    val fourth = fiveDay?.list?.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(
            getNextDays(4, zone, REGION_FORMAT)[3]
        )
    }
    val fifth = fiveDay?.list?.filter {
        it.dt.timeConverter(REGION_FORMAT, zone).contains(
            getNextDays(5, zone, REGION_FORMAT)[4]
        )
    }
    Column(modifier = modifier) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = containerColor()
        ) {
            days.forEachIndexed { index, title ->
                val text = if (index == 0) "Today" else title
                Tab(
                    text = {
                        DarkText(
                            text = text,
                            fontSize = 13.sp,
                            color = textColor(),
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
        HorizontalPager(count = days.size, state = pagerState) {
            val list = when (it) {
                0 -> first
                1 -> second
                2 -> third
                3 -> fourth
                4 -> fifth
                else -> first
            }
            list?.let { five -> DailyDetail(day = five, settings = settings, zone = zone) }
        }
    }
}

@Composable
fun DailyDetail(day: List<Day>, settings: AppSettings, zone: String) {
    LazyColumn {
        items(day.size) {
            val item = day[it]
            val temperature = item.main.temp.setTemperature(settings.temperature)
            val wind = item.wind.speed.setWindSpeed(settings.wind)
            val pressure = item.main.pressure.setPressure(settings.pressure)
            val clouds = "${item.clouds.all}%"
            val visibility = item.visibility.setVisibility(settings.visibility)
            val conditionText = item.weather.first().description
            val feelsLike = item.main.feels_like.setTemperature(settings.temperature)
            val tempIcon = setIconWidget(item.weather.first().icon)
            BackgroundContainer {
                DarkText(
                    text = item.dt.timeConverter(HOUR_FORMAT, zone),
                    color = textColor(),
                    modifier = Modifier.padding(12.dp)
                )
                Divide()
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (temp, realFeel, icon, condition) = createRefs()
                    Text(text = temperature,
                        color = Color.White,
                        fontSize = 66.sp,
                        modifier = Modifier.constrainAs(temp) {
                            start.linkTo(parent.start, margin = 24.dp)
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
                    Text(
                        text = feelsLike,
                        color = Color.White,
                        modifier = Modifier.constrainAs(realFeel) {
                            top.linkTo(temp.bottom, margin = 8.dp)
                            start.linkTo(temp.start)
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
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        LightText(text = stringResource(id = R.string.humidity))
                        LightText(text = stringResource(id = R.string.wind))
                        LightText(text = stringResource(id = R.string.pressure))
                        LightText(text = stringResource(id = R.string.clouds))
                        LightText(text = stringResource(id = R.string.visibility))
                    }
                    Column {
                        DarkText(text = "${item.main.humidity}%")
                        DarkText(text = wind)
                        DarkText(text = pressure)
                        DarkText(text = clouds)
                        DarkText(text = visibility)
                    }
                }
            }
        }
    }
}