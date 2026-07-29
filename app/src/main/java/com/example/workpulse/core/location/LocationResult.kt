package com.example.workpulse.core.location

data class LocationResult(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val address: String? = null,
)
