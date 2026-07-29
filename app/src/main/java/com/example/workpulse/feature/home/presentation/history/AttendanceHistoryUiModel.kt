package com.example.workpulse.feature.home.presentation.history

data class AttendanceHistoryUiModel(

    val date: String,

    val day: String,

    val punchIn: String,

    val punchOut: String,

    val punchInLocation: String,

    val punchOutLocation: String,

    val totalHours: String,

    val status: AttendanceStatus

)

enum class AttendanceStatus {

    PRESENT,

    ABSENT,

    HALF_DAY

}