package com.example.workpulse.feature.config

object WorkPulseFeatureRegistry {
    const val ATTENDANCE = "ATTENDANCE"
    const val ATTENDANCE_HISTORY = "ATTENDANCE_HISTORY"
    const val LEAVE = "LEAVE"
    const val LEAVE_HISTORY = "LEAVE_HISTORY"
    const val LEAVE_APPLICATION = "LEAVE_APPLICATION"
    const val COMP_OFF = "COMP_OFF"
    const val COMPOFF_HISTORY = "COMPOFF_HISTORY"

    private val supportedFeatures = setOf(
        ATTENDANCE,
        ATTENDANCE_HISTORY,
        LEAVE,
        LEAVE_HISTORY,
        LEAVE_APPLICATION,
        COMP_OFF,
        COMPOFF_HISTORY
    )

    fun isSupported(featureKey: String) : Boolean {
        return supportedFeatures.contains(featureKey.uppercase())
    }
}

