@file:OptIn(ExperimentalMaterial3Api::class)

package android.weather.compose.presentation

import android.app.Activity
import android.weather.compose.MainActivity
import android.weather.compose.R
import android.weather.compose.localization.LocalizationActivityDelegate
import android.weather.compose.setting.TinyDB
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.backgroundColor
import android.weather.compose.ui.theme.containerColor
import android.weather.compose.utils.LAST_LANGUAGE
import android.weather.compose.utils.context
import android.weather.compose.utils.gotoActivity
import android.weather.compose.view_model.MainViewModel
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Activity.LanguageScreen(
    navController: NavController,
    viewModel: MainViewModel,
    localizationDelegate: LocalizationActivityDelegate,
) {
    val context = LocalContext.current
    Scaffold(
        containerColor = backgroundColor(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor()),
                title = {
                    DarkText(
                        text = stringResource(id = R.string.choose_language),
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
                }, actions = {
                    IconButton(onClick = {
                        localizationDelegate.setLanguage(
                            context,
                            viewModel.language
                        )
                        finish()
                        gotoActivity<MainActivity>()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            LanguageContent(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel
            )
        })
}

@Composable
fun LanguageContent(
    modifier: Modifier,
    viewModel: MainViewModel,
) {
    val tinyDB = TinyDB(context())
    var selectedPosition by remember { mutableIntStateOf(tinyDB.getInt(LAST_LANGUAGE, 0)) }
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(languages.size) {
            val item = languages[it]
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                    viewModel.language = item.code
                    selectedPosition = it
                    tinyDB.putInt(LAST_LANGUAGE, it)
                }
            ) {
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    modifier = Modifier
                        .padding(start = 11.dp)
                        .size(21.dp)
                )
                DarkText(
                    text = item.title, modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(0.5f)
                )
                RadioButton(
                    selected = selectedPosition == it,
                    onClick = {
                        viewModel.language = item.code
                        selectedPosition = it
                        tinyDB.putInt(LAST_LANGUAGE, it)
                    }
                )
            }
        }
    }
}

data class Localization(
    val title: String,
    @DrawableRes val icon: Int,
    val code: String,
    var isChecked: Boolean = false,
)

val languages =
    listOf(
        Localization("English", R.drawable.english_flag, "en"),
        Localization("العربية", R.drawable.arabic_flag, "ar"),
        Localization("বাংলা", R.drawable.bangladeshi_flag, "bn"),
        Localization("汉语", R.drawable.chinese_flag, "zh"),
        Localization("Deutsch", R.drawable.german_flag, "de"),
        Localization("हिंदी", R.drawable.indian_flag, "hi"),
        Localization("Indonesian", R.drawable.indonesian_flag, "in"),
        Localization("Italiano", R.drawable.italian_flag, "it"),
        Localization("Melany", R.drawable.malaysian_flag, "ms"),
        Localization("Dutch", R.drawable.dutch_flag, "nl"),
        Localization("Русский", R.drawable.russian_flag, "ru"),
        Localization("한국어", R.drawable.korean_flag, "ko"),
        Localization("Español", R.drawable.spanish_flag, "es"),
        Localization("Türkçe", R.drawable.turkish_flag, "tr"),
        Localization("Yкраїнська", R.drawable.ukraine_flag, "ru"),
        Localization("Português", R.drawable.portugese_flag, "pt"),
        Localization("ไทย", R.drawable.thailand_flag, "th"),
        Localization("日本語", R.drawable.japanese_flag, "ja"),
        Localization("Tiếng Việt", R.drawable.vietnam_flag, "vi")
    )