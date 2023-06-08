package android.weather.compose.localization

import android.app.Service
import android.content.Context
import android.content.res.Resources

abstract class LocalizationService : Service() {
    private val localizationDelegate: LocalizationServiceDelegate by lazy {
        LocalizationServiceDelegate(this)
    }

    override fun getBaseContext(): Context {
        return localizationDelegate.getBaseContext(super.getBaseContext())
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }
}