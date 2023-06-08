package android.weather.compose.utils

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

val Context.fusedLocationProviderClient
    get() = LocationServices.getFusedLocationProviderClient(this)

val locationRequest =
    LocationRequest.Builder(0L).setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setIntervalMillis(0)
        .setMinUpdateIntervalMillis(0)
        .setMaxUpdates(1)
        .build()

fun Context.isLocationEnabled(): Boolean {
    val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}