package android.weather.compose

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.weather.compose.di.databaseModule
import android.weather.compose.di.networkModule
import android.weather.compose.di.repositoryModule
import android.weather.compose.di.sharedPreferenceModule
import android.weather.compose.di.viewModelModule
import android.weather.compose.di.workerModule
import android.weather.compose.localization.LocalizationApplication
import android.weather.compose.notification.NotificationWorker
import android.weather.compose.setting.dataStore
import android.weather.compose.utils.CHANNEL_ID
import android.weather.compose.utils.scope
import android.weather.compose.view_model.MainViewModel
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import java.util.Locale
import java.util.concurrent.TimeUnit

class WeatherApplication : LocalizationApplication() {
    val viewModel by inject<MainViewModel>()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            modules(
                listOf(
                    networkModule, databaseModule, repositoryModule,
                    viewModelModule, sharedPreferenceModule, workerModule
                )
            )
            workManagerFactory()
        }
        createNotification()
        scope.launch {
            dataStore.data.collect {
                sendPeriodicNotification(it.notification)
            }
        }
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "weather", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true)
            channel.description = "weather updates"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendPeriodicNotification(interval: Long) {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
        val periodWork = PeriodicWorkRequestBuilder<NotificationWorker>(
            interval,
            TimeUnit.HOURS
        ).addTag("periodic-pending-notification")
            .setConstraints(constraints.build())
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "periodic-pending-notification",
            ExistingPeriodicWorkPolicy.KEEP,
            periodWork
        )
    }

    override fun getDefaultLanguage(context: Context): Locale = Locale.getDefault()
}