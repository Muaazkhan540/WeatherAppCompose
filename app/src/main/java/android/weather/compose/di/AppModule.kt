package android.weather.compose.di

import android.app.Application
import android.weather.compose.db.WeatherDatabase
import android.weather.compose.notification.NotificationWorker
import android.weather.compose.repo.MainRepository
import android.weather.compose.service.WeatherService
import android.weather.compose.setting.TinyDB
import android.weather.compose.view_model.MainViewModel
import androidx.room.Room
import androidx.work.WorkerParameters
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

val sharedPreferenceModule = module {
//    single { NotificationService(androidContext()) }
    single(createdAtStart = true) { TinyDB(androidContext()) }
}

val networkModule = module {
    single(createdAtStart = true) { WeatherService() }
}

val workerModule = module {
    worker { (workerParams: WorkerParameters) ->
        NotificationWorker(
            context = androidContext(),
            viewModel = get(),
            parameters = workerParams
        )
    }
}

val repositoryModule = module {
    single(createdAtStart = true) { MainRepository(get(), get()) }
}

val databaseModule = module {
    single { createWeatherDatabase(androidApplication()) }
    single { get<WeatherDatabase>().weatherDao() }
}

fun createWeatherDatabase(application: Application) =
    Room.databaseBuilder(application, WeatherDatabase::class.java, "weather_database_compose")
        .fallbackToDestructiveMigration()
        .build()