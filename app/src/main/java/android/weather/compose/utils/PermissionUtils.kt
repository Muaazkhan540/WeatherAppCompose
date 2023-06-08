package android.weather.compose.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.IntRange
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.hasLocationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        hasPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        hasPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }
}

fun Context.hasPermission(vararg permissions: String): Boolean {
    return permissions.all { singlePermission ->
        ContextCompat.checkSelfPermission(
            this,
            singlePermission
        ) == PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.requestPermission(vararg permissions: String, @IntRange(from = 0) requestCode: Int) =
    ActivityCompat.requestPermissions(this, permissions, requestCode)