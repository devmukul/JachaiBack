package com.jachai.jachai_driver.utils

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.util.*
import com.jachai.jachaimart.R
class JachaiLocationManager(val activity: AppCompatActivity) {

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val fusedClient = LocationServices.getFusedLocationProviderClient(activity)
    private var onLocationResultUpdate: ((location: CurrentLocation?) -> Unit)? = null

    private val requestLocationPermission =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[FINE_LOCATION] == true && permissions[COARSE_LOCATION] == true) {
                tryToEnableGps()
            } else {
                ToastUtils.warning(activity.resources.getString(R.string.location_permission_denied))
                onLocationResultUpdate?.invoke(null)
            }
        }

    private val requestEnableGPS =
        activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == FragmentActivity.RESULT_OK) {
                getDeviceLocation()
            } else {
                ToastUtils.warning(activity.resources.getString(R.string.location_permission_denied))
                onLocationResultUpdate?.invoke(null)
            }
        }

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun checkOrRequestPermission() {

        val permissions = arrayOf(FINE_LOCATION, COARSE_LOCATION)

        val isFineLocationPermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val isCoarseLocationPermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isFineLocationPermissionGranted && isCoarseLocationPermissionGranted) {
            JachaiLog.e("JACHAI ","AllPermission Granted")
            tryToEnableGps()
        } else {
            requestLocationPermission.launch(permissions)
        }
    }

    private fun tryToEnableGps() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            getDeviceLocation()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    requestEnableGPS.launch(
                        IntentSenderRequest.Builder(exception.resolution.intentSender).build()
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    private fun getDeviceLocation() {

        try {
            fusedClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    JachaiLog.e("JACHAI ","Lat1: ${location.latitude}, long1: ${location.longitude}")
                    onLocationResultUpdate?.invoke(
                        CurrentLocation(
                            location.latitude,
                            location.longitude,
                            getAddressFromLocation(location)
                        )
                    )
                } else {
                    //last known location is null. Trying to capture location updates
                    getDeviceLocationFallBack()
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            onLocationResultUpdate?.invoke(null)
        }
    }

    private fun getDeviceLocationFallBack() {
        try {
            //ToastUtils.normal(activity.resources.getString(R.string.fetching_device_location))
            val updateListener = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    super.onLocationResult(result)
                    if (result == null) {
                        onLocationResultUpdate?.invoke(null)
                        return
                    }
                    fusedClient.removeLocationUpdates(this)

                    val location = result.lastLocation
                    JachaiLog.e("JACHAI ","Lat2: ${location.latitude}, long2: ${location.longitude}")
                    onLocationResultUpdate?.invoke(
                        CurrentLocation(
                            location.latitude,
                            location.longitude,
                            getAddressFromLocation(location)
                        )
                    )
                }
            }
            fusedClient.requestLocationUpdates(
                locationRequest,
                updateListener,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            onLocationResultUpdate?.invoke(null)
        }
    }

    private fun getAddressFromLocation(location: Location): String {
        return try {
            val geocoder = Geocoder(activity, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (addresses.isNotEmpty()) {

                //val userCountry = addresses[0].getCountryName()
                addresses[0].getAddressLine(0)
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun fetchCurrentLocation(onLocationResultUpdate: (location: CurrentLocation?) -> Unit) {
        this.onLocationResultUpdate = onLocationResultUpdate
        checkOrRequestPermission()
    }

    fun removeLocationCallBack() {
        onLocationResultUpdate = null
    }
}