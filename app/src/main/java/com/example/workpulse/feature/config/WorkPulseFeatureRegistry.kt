package com.example.workpulse.feature.config

object WorkPulseFeatureRegistry {
    const val ATTENDANCE = "ATTENDANCE"
    const val ATTENDANCE_HISTORY = "ATTENDANCE_HISTORY"
    const val LEAVE = "LEAVE"
    const val LEAVE_HISTORY = "LEAVE_HISTORY"
    const val LEAVE_APPLICATION = "LEAVE_APPLICATION"
    const val COMPOFF_APPLICATION = "COMPOFF_APPLICATION"
    const val COMPOFF_HISTORY = "COMPOFF_HISTORY"

    private val supportedFeatures = setOf(
        ATTENDANCE,
        ATTENDANCE_HISTORY,
        LEAVE,
        LEAVE_HISTORY,
        LEAVE_APPLICATION,
        COMPOFF_APPLICATION,
        COMPOFF_HISTORY,

    )

    fun isSupported(featureKey: String) : Boolean {
        return supportedFeatures.contains(featureKey.uppercase())
    }
}

