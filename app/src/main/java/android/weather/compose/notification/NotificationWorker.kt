package android.weather.compose.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.weather.compose.MainActivity
import android.weather.compose.R
import android.weather.compose.models.forecast.WeatherForecast
import android.weather.compose.setting.AppSettings
import android.weather.compose.setting.dataStore
import android.weather.compose.utils.CHANNEL_ID
import android.weather.compose.utils.fusedLocationProviderClient
import android.weather.compose.utils.hasLocationPermission
import android.weather.compose.utils.scope
import android.weather.compose.utils.setIconDay
import android.weather.compose.utils.setIconNight
import android.weather.compose.utils.setTemperature
import android.weather.compose.view_model.MainViewModel
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NotificationWorker(
    private val context: Context,
    private val viewModel: MainViewModel,
    parameters: WorkerParameters,
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        return try {
            fetchLocation()
            initNotification()
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "doWork: ${e.message}")
            Result.retry()
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        if (context.hasLocationPermission()) {
            context.fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                task.result?.let {
                    viewModel.currentWeather(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun initNotification() {
        val weather = viewModel.weatherState
        scope.launch(Dispatchers.Default) {
            context.dataStore.data.collect { settings ->
                weather.collect { current ->
                    current.success?.let { weather ->
                        createNotification(
                            weather, settings
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("NotificationPermission")
    private fun createNotification(weather: WeatherForecast, settings: AppSettings) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        }
        val activityPendingIntent = PendingIntent.getActivity(
            context, 1, activityIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val current = weather.current
        val temp = settings.temperature.let { current?.temp_c?.setTemperature(it) }
        val code = current?.condition?.code ?: 1000
        val isDay = current?.is_day
        val icon =
            if (isDay == 1) context.setIconDay(code)?.toBitmap() else context.setIconNight(code)
                ?.toBitmap()
        val condition = current?.condition?.text
        val location = weather.location?.name
        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.splash_icon)
            setContentText(context.getString(R.string.condition, condition))
            setContentIntent(activityPendingIntent)
            setContentTitle(context.getString(R.string.notification_title, temp, location))
            setLargeIcon(icon)
            setSilent(true)
            setAutoCancel(true)
        }.build()
        notificationManager.notify(1, notification)
    }
}

private const val TAG = "NotificationWorker"