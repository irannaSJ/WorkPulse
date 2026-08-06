package com.example.workpulse.feature.attendance.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.workpulse.data.local.entity.LocationStatus

@Entity(
    tableName = "attendance",
    indices = [
        Index(value = ["employeeId"]),
        Index(value = ["attendanceDate"])
    ]
)
data class AttendanceEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /**
     * Employee ID from EmployeeEntity
     */
    val employeeId: String,

    /**
     * Format: yyyy-MM-dd
     * Example: 2026-07-22
     */
    val attendanceDate: String,

    /**
     * Epoch milliseconds
     */
    val punchInTime: Long? = null,

    /**
     * Epoch milliseconds
     */
    val punchOutTime: Long? = null,

    /**
     * Total working duration in seconds
     */
    val workingSeconds: Long = 0,

    /**
     * Current attendance state
     */
    val status: AttendanceStatus,

    /**
     * Sync status with ERPNext
     */
    val punchInSyncStatus: SyncStatus,

    val punchOutSyncStatus: SyncStatus ,

    /**
     * Local creation time
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * Last modification time
     */
    val updatedAt: Long = System.currentTimeMillis(),

    val latitude : Double? = null,

    val logitude :Double? = null,
    val accuracy : Float ?= null,
    val deviceId : String = "",
    val locationStatus : LocationStatus = LocationStatus.PENDING,
    val location: String = ""

//    val logType : LogType
)

enum class AttendanceStatus {

    NOT_PUNCHED_IN,

    PUNCHED_IN,

    PUNCHED_OUT
}


enum class SyncStatus {

    PENDING,

    SYNCED,

    FAILED,
    NOT_REQUIRED
}

enum class LogType{
    IN,
    OUT
}


