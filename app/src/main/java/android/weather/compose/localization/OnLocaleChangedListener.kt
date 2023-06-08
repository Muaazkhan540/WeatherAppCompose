package android.weather.compose.localization

interface OnLocaleChangedListener {
    fun onBeforeLocaleChanged()

    fun onAfterLocaleChanged()
}