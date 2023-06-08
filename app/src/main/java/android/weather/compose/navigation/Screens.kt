package android.weather.compose.navigation

import android.weather.compose.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

sealed class Screens(val route: String, val title: String) {

    sealed class DrawerScreens(
        route: String,
        val icon: ImageVector,
        title: String,
    ) : Screens(route, title) {
        object Settings : DrawerScreens("settings", Icons.Default.Settings, "Settings")
    }
}

val screensFromDrawer = listOf(
    Screens.DrawerScreens.Settings,
)

@Composable
fun title(screens: Screens) = when (screens) {
    is Screens.DrawerScreens.Settings -> stringResource(id = R.string.action_settings)
}