@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package android.weather.compose.setting

import android.weather.compose.Graph
import android.weather.compose.R
import android.weather.compose.presentation.RatingDialog
import android.weather.compose.presentation.languages
import android.weather.compose.ui.theme.BackgroundContainer
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.LightText
import android.weather.compose.ui.theme.PopupBg
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.containerColor
import android.weather.compose.utils.LAST_LANGUAGE
import android.weather.compose.utils.Space
import android.weather.compose.utils.context
import android.weather.compose.utils.onShareAppClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
fun SettingUi(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(containerColor())
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = backgroundColor(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor()),
                title = {
                    DarkText(
                        text = stringResource(id = R.string.action_settings),
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
            SettingsContent(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        })
}

@Composable
fun SettingsContent(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        BackgroundContainer(columnScope = {
            LightText(
                text = stringResource(id = R.string.app_settings),
                modifier = Modifier.padding(horizontal = 10.7.dp, vertical = 10.dp),
            )
            Divider()
            NotificationSection()
            Divider()
            SettingSection(
                text = stringResource(id = R.string.units),
                icon = R.drawable.ic_units,
                onClick = {
                    navController.navigate(Graph.UNITS)
                })
            Divider()
            LanguageSection(navController = navController)
        })
        Space()
        BackgroundContainer(columnScope = {
            LightText(
                text = stringResource(id = R.string.general_settings),
                modifier = Modifier.padding(horizontal = 10.7.dp, vertical = 10.dp),
            )
            Divider()
            SettingSection(text = stringResource(id = R.string.share_app),
                icon = R.drawable.ic_dr_share_01,
                onClick = {
                    context.onShareAppClick()
                })
            Divider()
            var dialog by remember {
                mutableStateOf(false)
            }
            RatingDialog(dialog = dialog) {
                dialog = false
            }
            SettingSection(text = stringResource(id = R.string.rate_us),
                icon = R.drawable.ic_star_2,
                onClick = { dialog = true })
        })
    }
}

@Composable
fun NotificationSection() {
    val settings = context().dataStore.data.collectAsState(
        initial = AppSettings()
    ).value
    var showMenu by remember {
        mutableStateOf(false)
    }
    var offset by remember {
        mutableStateOf(IntSize.Zero)
    }
    NotificationDropDown(
        settings = settings,
        expanded = showMenu,
        onShow = { showMenu = false },
        onDismiss = { showMenu = false },
        offset = offset
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.3.dp, horizontal = 10.7.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.notification_01),
                contentDescription = "ic_theme",
            )
            Column(
                modifier = Modifier.padding(start = 17.7.dp)
            ) {
                DarkText(
                    text = stringResource(id = R.string.notification),
                )
                LightText(
                    text = "Receive notification every ${settings.notification} hour",
                    modifier = Modifier.padding(end = 12.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        IconButton(
            onClick = { showMenu = true },
            modifier = Modifier.onSizeChanged { offset = it }) {
            Icon(
                Icons.Default.MoreVert, contentDescription = "ic_drop_down"
            )
        }
    }
}

@Composable
fun SettingSection(text: String, icon: Int, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 18.3.dp, horizontal = 10.7.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row {
            Image(
                painter = painterResource(id = icon),
                contentDescription = text,
            )
            DarkText(
                text = text, modifier = Modifier.padding(start = 17.7.dp)
            )
        }
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "more_vert")
    }
}

@Composable
fun LanguageSection(navController: NavController) {
    val tinyDB = TinyDB(context())
    val position = tinyDB.getInt(LAST_LANGUAGE, 0)
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Graph.LANGUAGE)
            }
            .padding(vertical = 18.3.dp, horizontal = 10.7.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_locale),
                contentDescription = "ic_locale",
            )
            DarkText(
                text = stringResource(id = R.string.languages),
                modifier = Modifier.padding(start = 17.7.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            DarkText(
                text = languages[position].title,
                modifier = Modifier.padding(start = 17.7.dp)
            )
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "more_vert")
        }
    }
}

@Composable
fun NotificationDropDown(
    settings: AppSettings,
    expanded: Boolean,
    offset: IntSize,
    onShow: () -> Unit,
    onDismiss: () -> Unit,
) {
    val notification = settings.notification
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(x = offset.width.dp / 4, y = (offset.height / 2.5).dp),
            onDismissRequest = { onDismiss() },
        ) {
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.four_hours)) },
                onClick = {
                    scope.launch {
                        context.updateNotification(4)
                    }
                    onShow()
                },
                modifier = Modifier.background(
                    color = if (notification == 4L) PopupBg else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { DarkText(text = stringResource(id = R.string.eight_hours)) },
                onClick = {
                    scope.launch {
                        context.updateNotification(8)
                    }
                    onShow()
                },
                modifier = Modifier.background(
                    color = if (notification == 8L) PopupBg else Color.Transparent
                )
            )
            DropdownMenuItem(text = { DarkText(text = stringResource(id = R.string.twelve_hours)) },
                modifier = Modifier.background(
                    color = if (notification == 12L) PopupBg else Color.Transparent
                ),
                onClick = {
                    scope.launch {
                        context.updateNotification(12)
                    }
                    onShow()
                })
        }
    }
}