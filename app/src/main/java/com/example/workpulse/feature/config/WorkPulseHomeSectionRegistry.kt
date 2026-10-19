package com.example.workpulse.feature.config

object WorkPulseHomeSectionRegistry {
    const val DATE_TIME = "DATE_TIME"
    const val ATTENDANCE = "ATTENDANCE"
    const val QUICK_ACTIONS = "QUICK_ACTIONS"
    const val LEAVE_SUMMARY = "LEAVE_SUMMARY"
//    const val ATTENDANCE_HISTORY = "ATTENDANCE_HISTORY"
//    const val LEAVE_HISTORY = "LEAVE_HISTORY"
//    const val PROFILE = "PROFILE"

    private val supportedSections = setOf(
        DATE_TIME,
        ATTENDANCE,
        QUICK_ACTIONS,
        LEAVE_SUMMARY,
//        ATTENDANCE_HISTORY,
//        LEAVE_HISTORY,
//        PROFILE
    )
    fun isSupported(sectionType: String): Boolean {
        return supportedSections.contains(sectionType.uppercase())
    }
}