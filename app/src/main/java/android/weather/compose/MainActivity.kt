@file:OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)

package android.weather.compose

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.weather.compose.localization.LocalizationActivity
import android.weather.compose.presentation.setContentDayColor
import android.weather.compose.presentation.setContentNightColor
import android.weather.compose.setting.TinyDB
import android.weather.compose.ui.theme.*
import android.weather.compose.utils.*
import android.weather.compose.view_model.MainViewModel
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.*
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URL
import java.util.*

class MainActivity : LocalizationActivity() {
    private val tinyDb by inject<TinyDB>()
    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val permissionState = rememberMultiplePermissionsState(
                        permissions = listOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else toString()
                        )
                    )
                    val code = currentCode(viewModel = viewModel)
                    if (isDay(viewModel = viewModel) == 1) setContentDayColor(code)
                    else setContentNightColor(code)

                    PermissionState(permission = permissionState, onGranted = {
                        initLocation()
                        RootNavigationGraph(
                            navController = rememberNavController(),
                            viewModel = viewModel, localizationDelegate = localizationDelegate
                        ) { finish() }
                    }, onRequest = {
                        RequestPermission {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    })
                }
            }
        }
    }

    @Composable
    fun RequestPermission(onClick: () -> Unit) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 170.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.five_stars),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.permission_required),
                fontFamily = poppinsMedium,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(id = R.string.permission_required_description),
                fontFamily = poppins,
                textAlign = TextAlign.Center
            )
        }
        Column(verticalArrangement = Arrangement.Bottom) {
            Button(modifier = Modifier
                .padding(bottom = 50.dp, start = 34.dp, end = 34.dp)
                .fillMaxWidth(), onClick = { onClick() }) {
                Text(
                    text = stringResource(id = R.string.allow),
                    fontSize = 21.sp,
                    fontFamily = poppinsMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocation() {
        if (isLocationEnabled()) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                task.result?.let {
                    viewModel.coordinates.value = Pair(first = it.latitude, second = it.longitude)
                    viewModel.currentCoordinates.value =
                        Pair(first = it.latitude, second = it.longitude)
                    fetchCurrentAddress(it.latitude, it.longitude)
                    tinyDb.putDouble(CURRENT_LAT, it.latitude)
                    tinyDb.putDouble(CURRENT_LON, it.longitude)
                } ?: newLocationData()
            }
        } else {
            scope.launch {
                viewModel.publicIp.value = getPublicIpAsync().await()
                viewModel.ipCoordinates.collect {
                    val lat = tinyDb.getDouble(CURRENT_LAT, it.first)
                    val lon = tinyDb.getDouble(CURRENT_LON, it.second)
                    viewModel.coordinates.value = Pair(lat, lon)
                }
            }
            locationDialog()
        }
    }

    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult())
        { activityResult ->
            if (activityResult.resultCode == RESULT_OK && isLocationEnabled()) {
                scope.launch(Dispatchers.IO) {
                    initLocation()
                }
            }
        }

    private fun locationDialog() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    resolutionForResult.launch(intentSenderRequest)

                } catch (sendEx: IntentSender.SendIntentException) {
                    "createLocationRequest: ${sendEx.message}"
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun newLocationData() {
        locationRequest.apply {
            Looper.myLooper()?.let {
                fusedLocationProviderClient.requestLocationUpdates(
                    this, locationCallback, it
                )
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            location?.let {
                viewModel.coordinates.value = Pair(it.latitude, it.longitude)
            }
        }
    }

    private suspend fun getPublicIpAsync(): Deferred<String> = coroutineScope {
        async(Dispatchers.IO) {
            val result: String = try {
                val url = URL("https://api.ipify.org")
                val httpsURLConnection = url.openConnection()
                val iStream = httpsURLConnection.getInputStream()
                val buff = ByteArray(1024)
                val read = iStream.read(buff)
                String(buff, 0, read)
            } catch (e: Exception) {
                "error : $e"
            }
            return@async result
        }
    }

    @Composable
    fun PermissionState(
        permission: MultiplePermissionsState,
        onGranted: @Composable () -> Unit,
        onRequest: @Composable () -> Unit,
    ) {
        permission.permissions.let {
            when {
                it[0].status.isGranted && it[1].status.isGranted -> onGranted()
                !it[0].status.isGranted && !it[1].status.isGranted -> onRequest()
            }
        }
    }

    private fun fetchCurrentAddress(lat: Double, lon: Double) {
        scope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(this@MainActivity, Locale.ENGLISH)
            @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(lat, lon, 1) { addresses ->
                    addresses[0]?.let { address ->
                        viewModel.location.value = Pair(
                            first = address.subLocality ?: "",
                            second = address.locality ?: "Enter search here"
                        )
                    }
                }
            } else geocoder.getFromLocation(lat, lon, 1)?.let { addresses ->
                addresses[0]?.let { address ->
                    viewModel.location.value = Pair(
                        first = address.subLocality ?: "",
                        second = address.locality ?: "Enter search here"
                    )
                }
            }
        }
    }
}