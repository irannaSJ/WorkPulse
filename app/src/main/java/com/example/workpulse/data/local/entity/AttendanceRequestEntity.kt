package com.example.workpulse.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestType

/**
 * A locally persisted attendance regularization request.
 *
 * This is deliberately separate from [com.example.workpulse.feature.attendance.data.local.entity.AttendanceEntity]:
 * an attendance record is a captured event, while this entity is an employee's request
 * to correct or regularize one.
 */
//@Entity(
//    tableName = "attendance_request",
//    indices = [
//        Index(value = ["employeeId"]),
//        Index(value = ["attendanceDate"]),
//        Index(value = ["erpNextId"], unique = true)
//    ]
//)
//data class AttendanceRequestEntity(
//    @PrimaryKey(autoGenerate = true)
//    val id: Long = 0,
//    val erpNextId: String? = null,
//    val employeeId: String,
//    val attendanceDate: String,
//    val fromDate: Long? = null,
//    val toDate: Long? = null,
//    val reason: String,
//    val location : String = "",
//    val sourceAttendanceId: Long? = null,
//    val requestStatus: AttendanceRequestStatus = AttendanceRequestStatus.PENDING,
//    val syncStatus: SyncStatus = SyncStatus.PENDING,
//    val createdAt: Long = System.currentTimeMillis(),
//    val updatedAt: Long = System.currentTimeMillis()
//)
//


@Entity(
    tableName = "attendance_request",
    indices = [
        Index(value = ["employeeId"]),
        Index(value = ["attendanceDate"]),
        Index(value = ["erpNextId"], unique = true)
    ]
)
data class AttendanceRequestEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val erpNextId: String? = null,

    val employeeId: String,

    val attendanceDate: String,

    val fromDate: Long? = null,

    val toDate: Long? = null,

    // Explanation entered by employee
    val reason: String,

    // On Duty / Work From Home
    val requestType: AttendanceRequestType,

    // Include holidays
    val includeHolidays: Boolean = false,

    val sourceAttendanceId: Long? = null,

    val requestStatus: AttendanceRequestStatus =
        AttendanceRequestStatus.PENDING,

    val syncStatus: SyncStatus =
        SyncStatus.PENDING,

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long = System.currentTimeMillis(),
    val location :String = ""

)

enum class AttendanceRequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED
}
