@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class
)

package android.weather.compose.presentation

import android.view.Gravity
import android.weather.compose.Graph
import android.weather.compose.R
import android.weather.compose.navigation.screensFromDrawer
import android.weather.compose.navigation.title
import android.weather.compose.ui.theme.CloudyColor
import android.weather.compose.ui.theme.CloudyNightColor
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.DayColor
import android.weather.compose.ui.theme.HazeColor
import android.weather.compose.ui.theme.LightText
import android.weather.compose.ui.theme.NightColor
import android.weather.compose.ui.theme.WinterColor
import android.weather.compose.ui.theme.WinterNightColor
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.containerColor
import android.weather.compose.ui.theme.textColor
import android.weather.compose.utils.Space
import android.weather.compose.utils.poppins
import android.weather.compose.utils.poppinsMedium
import android.weather.compose.utils.tomorrowCode
import android.weather.compose.view_model.MainViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@Composable
fun MainUi(
    viewModel: MainViewModel,
    navController: NavController,
    action: () -> Unit,
) {
    NavDrawer(
        viewModel = viewModel,
        navController = navController,
        action = action
    )
}

@Composable
fun ViewPager(viewModel: MainViewModel, navController: NavController) {
    var currentTab by remember {
        mutableIntStateOf(0)
    }
    val systemUiController = rememberSystemUiController()
    val isDay = viewModel.weatherState.collectAsState().value.success?.current?.is_day ?: 1
    val code =
        viewModel.weatherState.collectAsState().value.success?.current?.condition?.code ?: 1000
    val tomorrowCode = tomorrowCode(viewModel)
    val currentColor = if (isDay == 1) setContentDayColor(code) else setContentNightColor(code)
    val tomorrowColor = setContentDayColor(tomorrowCode)
    val tabColor = when (currentTab) {
        0 -> currentColor
        1 -> tomorrowColor
        2 -> containerColor()
        else -> currentColor
    }
    viewModel.color = tabColor
    systemUiController.setStatusBarColor(tabColor)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val pages = listOf(
        stringResource(id = R.string.today),
        stringResource(id = R.string.tomorrow),
        stringResource(id = R.string.five_day)
    )
    androidx.compose.material.TabRow(
        modifier = Modifier.height(48.dp),
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = tabColor,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions), height = 2.dp,
                color = indicatorColor(
                    page = currentTab
                )
            )
        }) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        title, fontSize = 16.sp, fontFamily = poppins,
                        color = tabTextColor(page = currentTab)
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
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            currentTab = page
        }
    }
    HorizontalPager(
        count = pages.size,
        state = pagerState,
    ) { page ->
        when (page) {
            0 -> TodayUi(viewModel = viewModel, navController = navController)
            1 -> TomorrowMainUi(viewModel = viewModel, navController = navController)
            2 -> MainFiveDayUi(viewModel = viewModel)
        }
    }
}

@Composable
fun TopBar(viewModel: MainViewModel, drawerState: DrawerState) {
    val address = viewModel.location.collectAsState().value
    val scope = rememberCoroutineScope()
    Column {
        ConstraintLayout(
            modifier = Modifier
                .background(color = viewModel.color)
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(38.dp)
                .padding(horizontal = 20.dp)
                .background(shape = RoundedCornerShape(20.dp), color = backgroundColor())
                .padding(vertical = 8.dp)
        ) {
            val (drawerIcon, location) = createRefs()
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }, modifier = Modifier
                .constrainAs(drawerIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_drawer_01),
                    contentDescription = "drawer_icon"
                )
            }
            Text(
                modifier = Modifier
                    .constrainAs(location) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(drawerIcon.end)
                        end.linkTo(parent.end)
                        height = Dimension.wrapContent
                        width = Dimension.fillToConstraints
                    },
                text = "${address.first} ${address.second}",
                fontFamily = poppinsMedium,
                maxLines = 1,
                color = textColor(),
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun NavDrawer(
    viewModel: MainViewModel,
    navController: NavController,
    action: () -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    BackHandler {
        if (drawerState.isOpen) scope.launch {
            drawerState.close()
        }
        else action()
    }
    ModalNavigationDrawer(drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.8f)) {
                DrawerHeader()
                screensFromDrawer.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        shape = RectangleShape,
                        label = { Text(title(screens = item)) },
                        selected = false,
                        onClick = {
                            navController.navigate(Graph.DETAILS)
                            scope.launch { drawerState.close() }
                        },
                    )
                }
            }
        },
        content = {
            ScaffoldCompose(viewModel, drawerState, navController)
        })
}

@Composable
fun ScaffoldCompose(
    viewModel: MainViewModel,
    drawerState: DrawerState,
    navController: NavController,
) {
    Scaffold(topBar = {
        Column {
            TopBar(
                drawerState = drawerState,
                viewModel = viewModel
            )
            ViewPager(viewModel, navController = navController)
        }
    }, content = { innerPadding ->
        Box(
            // consume insets as scaffold doesn't do it by default
            modifier = Modifier.consumeWindowInsets(innerPadding),
        ) {

        }
    })
}

fun setContentDayColor(code: Int) =
    when (code) {
        1000 -> DayColor
        1003, 1006, 1009, 1063, 1072, 1087, 1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189,
        1192, 1195, 1198, 1201, 1240, 1243, 1246, 1273, 1276, 1279, 1282,
        -> CloudyColor

        1030, 1135, 1147 -> HazeColor
        1066, 1069, 1114, 1117, 1204, 1207, 1210, 1213, 1216, 1219, 1222, 1225, 1237, 1249,
        1252, 1255, 1258, 1261, 1264,
        -> WinterColor

        else -> DayColor
    }

fun setContentNightColor(code: Int) =
    when (code) {
        1000 -> NightColor
        1003, 1006, 1009, 1063, 1087, 1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189, 1192, 1195,
        1198, 1201, 1240, 1243, 1246, 1249, 1273, 1276, 1279, 1282,
        -> CloudyNightColor

        1030, 1135, 1147 -> HazeColor
        1066, 1069, 1072, 1114, 1117, 1204, 1207, 1210, 1213, 1216, 1219, 1222, 1225, 1237, 1252,
        1255, 1258, 1261, 1264,
        -> WinterNightColor

        else -> NightColor
    }

@Composable
fun tabTextColor(page: Int) = when (page) {
    0 -> Color.White
    1 -> Color.White
    2 -> Color(0xFF89909a)
    else -> Color.White
}

@Composable
fun indicatorColor(page: Int) = when (page) {
    0 -> Color.White
    1 -> Color.White
    2 -> Color.Black
    else -> Color.White
}

@Composable
fun RatingDialog(dialog: Boolean, onDismiss: () -> Unit) {
    val ratingIcon by remember {
        mutableIntStateOf(R.drawable.ic_dialog_star)
    }
    GeneralDialog(dialog = dialog, onDismiss = { onDismiss() }, columnScope = {
        IconButton(
            onClick = { onDismiss() }, modifier = Modifier
                .align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close",
                modifier = Modifier.size(18.7.dp)
            )
        }
        Image(
            painter = painterResource(id = ratingIcon),
            contentDescription = "ratingIcon",
            modifier = Modifier
                .size(82.7.dp)
        )
        Space(20)
        DarkText(text = stringResource(id = R.string.enjoying_weather_app))
        LightText(text = stringResource(id = R.string.gratefull_text))
        LightText(text = stringResource(id = R.string.rate_text))
        Button(
            onClick = { }, modifier = Modifier
                .padding(19.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.feedback))
        }
    })
}

@Composable
fun GeneralDialog(
    dialog: Boolean,
    onDismiss: () -> Unit,
    columnScope: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    roundedCornerShape: Dp = 13.dp,
    elevation: Dp = 8.dp,
    gravity: Int = Gravity.CENTER,
) {
    if (dialog)
        AlertDialog(onDismissRequest = { onDismiss() }) {
            val window = LocalView.current.parent as DialogWindowProvider
            window.window.setGravity(gravity)
            Card(
                shape = RoundedCornerShape(roundedCornerShape),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(backgroundColor()),
                    horizontalAlignment = alignment,
                ) {
                    columnScope(this)
                }
            }
        }
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        DarkText(text = stringResource(id = R.string.weather), fontSize = 30.sp)
    }
}

@Composable
fun LoadingContent() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}