@file:OptIn(ExperimentalMaterial3Api::class)

package android.weather.compose.setting

import android.weather.compose.R
import android.weather.compose.ui.theme.AppColor
import android.weather.compose.ui.theme.BackgroundContainer
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.PopupBg
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.containerColor
import android.weather.compose.utils.Space
import android.weather.compose.utils.context
import android.weather.compose.utils.scope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@Composable
fun UnitsUi(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(containerColor())
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val settings = context().dataStore.data.collectAsState(
        initial = AppSettings()
    ).value
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = backgroundColor(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor()),
                title = {
                    DarkText(
                        text = stringResource(id = R.string.units),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 20.sp
                    )
                }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }, scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            UnitsContent(settings = settings, modifier = Modifier.padding(innerPadding))
        })
}

@Composable
fun UnitsContent(settings: AppSettings, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Box {
            BackgroundContainer(columnScope = {

                Temperature(
                    unit = stringResource(id = R.string.temperature),
                    value = temperatureSummary(settings.temperature),
                    icon = R.drawable.ic_temperature_01,
                    settings = settings
                )
                Divider()
                Pressure(
                    unit = stringResource(id = R.string.pressure),
                    value = pressureSummary(settings.pressure),
                    icon = R.drawable.ic_pressure_01,
                    settings = settings
                )
                Divider()
                Wind(
                    unit = stringResource(id = R.string.wind),
                    value = windSummary(settings.wind),
                    icon = R.drawable.ic_wind_01,
                    settings = settings
                )
                Divider()
                Rain(
                    unit = stringResource(id = R.string.rain),
                    value = rainSummary(settings.precipitation),
                    icon = R.drawable.ic_rain_01,
                    settings = settings
                )
                Divider()
                Visibility(
                    unit = stringResource(id = R.string.visibility),
                    value = visibilitySummary(settings.visibility),
                    icon = R.drawable.ic_visibility_01,
                    settings = settings
                )
            })
        }
    }
}

@Composable
fun Temperature(
    unit: String,
    value: String,
    icon: Int,
    settings: AppSettings,
) {
    var temperature by remember {
        mutableStateOf(false)
    }
    var offset by remember {
        mutableStateOf(IntSize.Zero)
    }
    TemperatureDropDown(
        expanded = temperature,
        settings = settings,
        offset = offset,
        onShow = { temperature = false }) {
        temperature = false
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, end = 10.7.dp, start = 10.7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = icon), contentDescription = unit)
            Space(width = 17.7)
            Column {
                DarkText(text = unit)
                DarkText(text = value, color = AppColor)
            }
        }
        IconButton(
            onClick = { temperature = true },
            modifier = Modifier.onSizeChanged { offset = it }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = value)
        }
    }
}

@Composable
fun Pressure(unit: String, value: String, icon: Int, settings: AppSettings) {
    var pressure by remember {
        mutableStateOf(false)
    }
    var offset by remember {
        mutableStateOf(IntSize.Zero)
    }
    PressureDropDown(
        expanded = pressure,
        settings = settings,
        offset = offset,
        onShow = { pressure = false }) {
        pressure = false
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, end = 10.7.dp, start = 10.7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = icon), contentDescription = unit)
            Space(width = 17.7)
            Column {
                DarkText(text = unit)
                DarkText(text = value, overflow = TextOverflow.Ellipsis, color = AppColor)
            }
        }
        IconButton(
            onClick = { pressure = true },
            modifier = Modifier.onSizeChanged { offset = it }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = value)
        }
    }
}

@Composable
fun Wind(unit: String, value: String, icon: Int, settings: AppSettings) {
    var wind by remember {
        mutableStateOf(false)
    }
    var offset by remember {
        mutableStateOf(IntSize.Zero)
    }
    WindDropDown(expanded = wind, settings = settings, offset = offset, onShow = { wind = false }) {
        wind = false
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, end = 10.7.dp, start = 10.7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = icon), contentDescription = unit)
            Space(width = 17.7)
            Column {
                DarkText(text = unit)
                DarkText(text = value, color = AppColor)
            }
        }
        IconButton(onClick = { wind = true }, modifier = Modifier.onSizeChanged { offset = it }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = value)
        }
    }
}

@Composable
fun Rain(unit: String, value: String, icon: Int, settings: AppSettings) {
    var rain by remember {
        mutableStateOf(false)
    }
    var offset by remember {
        mutableStateOf(IntSize.Zero)
    }
    RainDropDown(expanded = rain, offset = offset, settings = settings, onShow = { rain = false }) {
        rain = false
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, end = 10.7.dp, start = 10.7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = icon), contentDescription = unit)
            Space(width = 17.7)
            Column {
                DarkText(text = unit)
                DarkText(text = value, color = AppColor)
            }
        }
        IconButton(onClick = { rain = true }, modifier = Modifier.onSizeChanged { offset = it }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = value)
        }
    }
}

@Composable
fun Visibility(unit: String, value: String, icon: Int, settings: AppSettings) {
    var visibility by remember {
        mutableStateOf(false)
    }
    var offset by remember {
        mutableStateOf(IntSize.Zero)
    }
    VisibilityDropDown(
        expanded = visibility,
        settings = settings, offset = offset,
        onShow = { visibility = false }) {
        visibility = false
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, end = 10.7.dp, start = 10.7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = icon), contentDescription = unit)
            Space(width = 17.7)
            Column {
                DarkText(text = unit)
                DarkText(text = value, color = AppColor)
            }
        }
        IconButton(
            onClick = { visibility = true },
            modifier = Modifier.onSizeChanged { offset = it }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = value)
        }
    }
}

@Composable
fun TemperatureDropDown(
    expanded: Boolean,
    settings: AppSettings,
    onShow: () -> Unit,
    offset: IntSize,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() },
            offset = DpOffset(x = offset.width.dp / 4, y = (offset.height / 2.5).dp)
        ) {
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.c_centigrade)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updateTemperature(temperature(0))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.temperature == Temperature.Centigrade)
                        PopupBg else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.f_fahrenheit)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updateTemperature(temperature(1))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.temperature == Temperature.Fahrenheit)
                        PopupBg else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.k_kelvin)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updateTemperature(temperature(2))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.temperature == Temperature.Kelvin)
                        PopupBg else Color.Transparent
                )
            )
        }
    }
}

@Composable
fun PressureDropDown(
    expanded: Boolean,
    settings: AppSettings,
    offset: IntSize,
    onShow: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() },
            offset = DpOffset(x = offset.width.dp / 4, y = (offset.height / 2.5).dp)
        ) {
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.psi_pound_force_per_square_inch)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updatePressure(pressure(0))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.pressure == Pressure.PoundForcePerSquareInch) PopupBg
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.mbar_millibar_pressure_unit)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updatePressure(pressure(1))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.pressure == Pressure.MillibarPressureUnit) PopupBg
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.inhg_inch_of_mercury)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updatePressure(pressure(2))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.pressure == Pressure.InchOfMercury) PopupBg
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.mmhg_millimeters_of_mercury)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updatePressure(pressure(3))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.pressure == Pressure.MillimetersOfMercury) PopupBg
                    else Color.Transparent
                )
            )
        }
    }
}

@Composable
fun WindDropDown(
    expanded: Boolean,
    settings: AppSettings,
    offset: IntSize,
    onShow: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() },
            offset = DpOffset(x = offset.width.dp / 4, y = (offset.height / 2.5).dp)
        ) {
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.km_h_kilometer_per_hour)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updateWind(wind(0))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.wind == Wind.KilometerPerHour) PopupBg
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.m_s_meters_per_second)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updateWind(wind(1))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.wind == Wind.MeterPerSec) PopupBg
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.mph_miles_per_hour)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updateWind(wind(2))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.wind == Wind.MilesPerHour) PopupBg
                    else Color.Transparent
                )
            )
        }
    }
}

@Composable
fun RainDropDown(
    expanded: Boolean,
    settings: AppSettings,
    offset: IntSize,
    onShow: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() },
            offset = DpOffset(x = offset.width.dp / 4, y = (offset.height / 2.5).dp)
        ) {
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.mm_millimeters)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updatePrecipitation(rain(0))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.precipitation == Precipitation.Millimeter) PopupBg
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.in_inches)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updatePrecipitation(rain(1))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.precipitation == Precipitation.Inches) PopupBg
                    else Color.Transparent
                )
            )
        }
    }
}

@Composable
fun VisibilityDropDown(
    expanded: Boolean,
    settings: AppSettings,
    offset: IntSize,
    onShow: () -> Unit,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() },
            offset = DpOffset(x = offset.width.dp / 4, y = (offset.height / 2.5).dp)
        ) {
            val context = LocalContext.current
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.km_kilometer)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updateVisibility(visibility(0))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.visibility == Visibility.KILOMETER) PopupBg
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.m_mile)) },
                onClick = {
                    onShow()
                    scope.launch {
                        context.updateVisibility(visibility(1))
                    }
                },
                modifier = Modifier.background(
                    color = if (settings.visibility == Visibility.MILES) PopupBg
                    else Color.Transparent
                )
            )
        }
    }
}

private fun temperatureSummary(temperature: Temperature): String {
    return when (temperature) {
        Temperature.Centigrade -> "°C (Centigrade)"
        Temperature.Fahrenheit -> "°F (Fahrenheit)"
        Temperature.Kelvin -> "K (Kelvin)"
    }
}

private fun pressureSummary(pressure: Pressure): String {
    return when (pressure) {
        Pressure.PoundForcePerSquareInch -> "psi (Pound force per square inch)"
        Pressure.MillibarPressureUnit -> "mbar (Millibar Pressure Unit)"
        Pressure.InchOfMercury -> "inHg (Inch of mercury)"
        Pressure.MillimetersOfMercury -> "mmHg (Millimeters of mercury)"
    }
}

private fun windSummary(wind: Wind): String {
    return when (wind) {
        Wind.KilometerPerHour -> "km/h (Kilometer per hour)"
        Wind.MeterPerSec -> "m/s (meters per second)"
        Wind.MilesPerHour -> "mph (Miles per hour)"
    }
}

private fun rainSummary(precipitation: Precipitation): String {
    return when (precipitation) {
        Precipitation.Millimeter -> "mm (Millimeters)"
        Precipitation.Inches -> "in (Inches)"
    }
}

private fun visibilitySummary(visibility: Visibility): String {
    return when (visibility) {
        Visibility.KILOMETER -> "km (Kilometer)"
        Visibility.MILES -> "m (Mile)"
    }
}

private fun temperature(value: Int) = Temperature.values()[value]
private fun wind(value: Int) = Wind.values()[value]
private fun pressure(value: Int) = Pressure.values()[value]
private fun visibility(value: Int) = Visibility.values()[value]
private fun rain(value: Int) = Precipitation.values()[value]