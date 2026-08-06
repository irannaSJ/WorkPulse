package com.example.workpulse.core.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import okio.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class ReverseGeocoder @Inject constructor(
    @ApplicationContext
    private val context : Context
){

    suspend fun getAddress(

        latitude: Double,

        longitude: Double

    ): String{
        val geocoder= Geocoder(
            context,
            Locale.getDefault()
        )

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                suspendCancellableCoroutine { continuation ->

                    geocoder.getFromLocation(

                        latitude,

                        longitude,

                        1

                    ) { addresses ->

                        continuation.resume(

                            addresses
                                .firstOrNull()
                                ?.toReadableAddress()
                                ?: ""

                        )

                    }

                }

            } else {

                @Suppress("DEPRECATION")

                val addresses = geocoder.getFromLocation(

                    latitude,

                    longitude,

                    1

                )

                addresses
                    ?.firstOrNull()
                    ?.toReadableAddress()
                    ?: ""

            }
        }catch (e : IOException){
            ""
        }catch (e : Exception){
            ""
        }
    }
}

private fun Address.toReadableAddress(): String {

    return listOfNotNull(

        subLocality,

        locality,

        adminArea,

        countryName

    ).joinToString(", ")

}