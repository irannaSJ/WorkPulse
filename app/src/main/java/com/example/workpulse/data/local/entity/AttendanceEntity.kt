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
    val employeeId: String,
    val attendanceDate: String,
    val punchInTime: Long? = null,
    val punchOutTime: Long? = null,
    val workingSeconds: Long = 0,
    val status: AttendanceStatus,
    val punchInSyncStatus: SyncStatus,

    val punchOutSyncStatus: SyncStatus ,
    val createdAt: Long = System.currentTimeMillis(),
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


