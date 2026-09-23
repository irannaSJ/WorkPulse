package com.example.workpulse.feature.config

object WorkPulseHomeSectionRegistry {
    const val DATE_TIME = "DATE_TIME"
    const val ATTENDANCE = "ATTENDANCE"
    const val QUICK_ACTIONS = "QUICK_ACTIONS"
    const val LEAVE_SUMMARY = "LEAVE_SUMMARY"

    private val supportedSections = setOf(
        DATE_TIME,
        ATTENDANCE,
        QUICK_ACTIONS,
        LEAVE_SUMMARY
    )
    fun isSupported(sectionType: String): Boolean {
        return supportedSections.contains(sectionType.uppercase())
    }
}