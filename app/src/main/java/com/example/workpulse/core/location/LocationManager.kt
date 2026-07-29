package com.example.workpulse.core.location

import android.Manifest
import android.content.Context
import android.location.LocationManager as AndroidLocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.workpulse.core.datastore.SessionManager
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationManager @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
) {

    fun hasLocationPermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

    }

    fun isLocationEnabled(): Boolean {

        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as AndroidLocationManager

        return manager.isProviderEnabled(AndroidLocationManager.GPS_PROVIDER)
                || manager.isProviderEnabled(AndroidLocationManager.NETWORK_PROVIDER)
    }

    suspend fun getCurrentLocation(): LocationResult? {

        if (!hasLocationPermission()) {
            return null
        }

        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(0)
            .build()



        return suspendCancellableCoroutine { continuation ->

            try {

                fusedLocationClient
                    .getCurrentLocation(request, null)
                    .addOnSuccessListener { location ->

                        if (location == null) {
                            continuation.resume(null)
                            return@addOnSuccessListener
                        }

                        continuation.resume(
                            LocationResult(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                accuracy = location.accuracy,
                            )
                        )
                    }
                    .addOnFailureListener {

                        continuation.resume(null)

                    }

            } catch (_: SecurityException) {

                continuation.resume(null)

            }

        }

    }


}