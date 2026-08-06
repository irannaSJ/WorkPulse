package com.example.workpulse.data.local.database

import androidx.room.TypeConverter
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.data.local.entity.LocationStatus
import com.example.workpulse.feature.attendance.data.local.entity.AttendanceStatus

import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus

class RoomConverters {

    @TypeConverter
    fun fromAttendanceRequestStatus(status: AttendanceRequestStatus): String =
        status.name

    @TypeConverter
    fun toAttendanceRequestStatus(value: String): AttendanceRequestStatus =
        AttendanceRequestStatus.valueOf(value)

    @TypeConverter
    fun fromAttendanceStatus(status: AttendanceStatus): String =
        status.name

    @TypeConverter
    fun toAttendanceStatus(value: String): AttendanceStatus =
        AttendanceStatus.valueOf(value)

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String =
        status.name

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus =
        SyncStatus.valueOf(value)

    @TypeConverter
    fun fromLocationStatus(status: LocationStatus): String {
        return status.name
    }

    @TypeConverter
    fun toLocationStatus(value: String): LocationStatus {
        return LocationStatus.valueOf(value)
    }
}
