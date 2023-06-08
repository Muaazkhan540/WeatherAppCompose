package android.weather.compose.localization

import android.content.Context

fun Context.toLocalizedContext(): Context = LocalizationUtility.getLocalizedContext(this)