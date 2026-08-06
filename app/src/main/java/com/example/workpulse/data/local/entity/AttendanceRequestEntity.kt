package com.example.workpulse.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus

/**
 * A locally persisted attendance regularization request.
 *
 * This is deliberately separate from [com.example.workpulse.feature.attendance.data.local.entity.AttendanceEntity]:
 * an attendance record is a captured event, while this entity is an employee's request
 * to correct or regularize one.
 */
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
    val requestedPunchInTime: Long? = null,
    val requestedPunchOutTime: Long? = null,
    val reason: String,
    val sourceAttendanceId: Long? = null,
    val requestStatus: AttendanceRequestStatus = AttendanceRequestStatus.PENDING,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class AttendanceRequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED
}
