package android.weather.compose

import android.app.Activity
import android.weather.compose.localization.LocalizationActivityDelegate
import android.weather.compose.navigation.Screens
import android.weather.compose.presentation.DetailScreen
import android.weather.compose.presentation.LanguageScreen
import android.weather.compose.presentation.MainUi
import android.weather.compose.presentation.SplashUi
import android.weather.compose.setting.SettingUi
import android.weather.compose.setting.UnitsUi
import android.weather.compose.view_model.MainViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@ExperimentalMaterial3Api
@Composable
fun Activity.RootNavigationGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
    localizationDelegate: LocalizationActivityDelegate,
    action: () -> Unit,
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.SPLASH
    ) {
        authNavGraph(navController = navController)
        composable(
            route = Graph.HOME
        ) {
            MainUi(
                viewModel = viewModel,
                navController = navController,
                action = action,
            )
        }
        detailsNavGraph(
            navController = navController,
            viewModel = viewModel,
            activity = this@RootNavigationGraph,
            localizationDelegate = localizationDelegate
        )
    }
}

fun NavGraphBuilder.detailsNavGraph(
    activity: Activity,
    navController: NavHostController,
    viewModel: MainViewModel,
    localizationDelegate: LocalizationActivityDelegate,
) {
    navigation(
        route = Graph.DETAILS,
        startDestination = Screens.DrawerScreens.Settings.route
    ) {
        composable(route = Screens.DrawerScreens.Settings.route) {
            SettingUi(navController = navController)
        }
        composable(route = Graph.UNITS) {
            UnitsUi(navController = navController)
        }
        composable(route = Graph.AIR_QUALITY) {
            AirQualityScree(navController = navController)
        }
        composable(route = Graph.DAILY) {
            DetailScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Graph.LANGUAGE) {
            activity.LanguageScreen(
                navController = navController,
                viewModel = viewModel,
                localizationDelegate = localizationDelegate
            )
        }
    }
}

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.SPLASH, startDestination = Graph.MAIN_SCREEN
    ) {
        composable(route = Graph.MAIN_SCREEN) {
            SplashUi {
                navController.popBackStack()
                navController.navigate(Graph.HOME)
            }
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val SPLASH = "splash_graph"
    const val HOME = "home_graph"
    const val MAIN_SCREEN = "main_screen"
    const val DETAILS = "details_graph"
    const val LANGUAGE = "language"
    const val DAILY = "daily_screen"
    const val UNITS = "unit"
    const val AIR_QUALITY = "air_quality"
}