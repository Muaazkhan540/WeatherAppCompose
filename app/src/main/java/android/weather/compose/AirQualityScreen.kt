@file:OptIn(ExperimentalMaterial3Api::class)

package android.weather.compose

import android.weather.compose.ui.theme.BackgroundContainer
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.FifthColor
import android.weather.compose.ui.theme.FirstColor
import android.weather.compose.ui.theme.FourthColor
import android.weather.compose.ui.theme.LightText
import android.weather.compose.ui.theme.SecColor
import android.weather.compose.ui.theme.ThirdColor
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.borderColor
import android.weather.compose.ui.theme.containerColor
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AirQualityScree(navController: NavController) {
    AirQualityUi(navController = navController)
}

@Composable
fun AirQualityUi(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(containerColor())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = backgroundColor(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor()),
                title = {
                    DarkText(
                        text = stringResource(id = R.string.air_quality),
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
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            AirQualityInfo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}

@Composable
fun AirQualityInfo(modifier: Modifier) {
    val airQualityList = listOf(
        AirQuality(
            icon = R.drawable.ic_wao_emoji,
            range = stringResource(id = R.string._0_50),
            title = stringResource(id = R.string.good),
            text = stringResource(id = R.string.good_air_quality),
            largeText = stringResource(id = R.string.none),
            color = FirstColor
        ),
        AirQuality(
            icon = R.drawable.ic_happy_emoji,
            range = stringResource(id = R.string._51_100),
            title = stringResource(id = R.string.moderate),
            text = stringResource(id = R.string.moderate_air_quality),
            largeText = stringResource(id = R.string.moderate_text_extra),
            color = SecColor
        ),
        AirQuality(
            icon = R.drawable.ic_cry_emoji,
            range = stringResource(id = R.string._101_150),
            title = stringResource(id = R.string.unhealthy),
            text = stringResource(id = R.string.unhealthy_air_quality),
            largeText = stringResource(id = R.string.unhealthy_text_extra),
            color = ThirdColor
        ),
        AirQuality(
            icon = R.drawable.ic_weep_emoji,
            range = stringResource(id = R.string._151_200),
            title = stringResource(id = R.string.very_unhealthy),
            text = stringResource(id = R.string.very_unhealthy_air_quality),
            largeText = stringResource(id = R.string.very_unhealthy_text_extra),
            color = FourthColor
        ),
        AirQuality(
            icon = R.drawable.ic_hazardous,
            range = stringResource(id = R.string._201_300),
            title = stringResource(id = R.string.hazardous),
            text = stringResource(id = R.string.hazardous_air_quality),
            largeText = stringResource(id = R.string.hazardous_text_extra),
            color = FifthColor
        )
    )
    LazyColumn(modifier = modifier) {
        items(airQualityList.size) {
            val item = airQualityList[it]
            BackgroundContainer(
                columnScope = {
                    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                modifier = Modifier
                                    .background(
                                        shape = RoundedCornerShape(12.dp), color = containerColor()
                                    )
                                    .border(
                                        width = 0.7.dp,
                                        color = borderColor(),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                painter = painterResource(id = item.icon),
                                contentDescription = "air_quality"
                            )
                            DarkText(
                                text = item.range,
                                modifier = Modifier
                                    .padding(start = 11.dp, end = 8.dp)
                                    .weight(0.5f),
                                fontSize = 24.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = item.title, fontSize = 13.sp,
                                color = Color.White, modifier = Modifier
                                    .padding(start = 9.3.dp, bottom = 8.dp)
                                    .background(
                                        shape = RoundedCornerShape(6.7.dp),
                                        color = item.color
                                    )
                                    .padding(horizontal = 4.7.dp, vertical = 2.5.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                        Text(text = item.text, modifier = Modifier.padding(top = 11.dp))
                        LightText(text = item.largeText, modifier = Modifier.padding(top = 11.dp))
                    }
                })
        }
    }
}

data class AirQuality(
    @DrawableRes val icon: Int,
    val range: String,
    val title: String,
    val text: String,
    val largeText: String,
    val color: Color,
)