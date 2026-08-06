package com.example.workpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workpulse.data.local.entity.AttendanceRequestEntity
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceRequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRequest(request: AttendanceRequestEntity): Long

    @Update
    suspend fun updateAttendanceRequest(request: AttendanceRequestEntity)

    @Query(
        """
        SELECT * FROM attendance_request
        WHERE employeeId = :employeeId
        ORDER BY attendanceDate DESC, createdAt DESC
        """
    )
    fun observeAttendanceRequests(employeeId: String): Flow<List<AttendanceRequestEntity>>

    @Query(
        """
        SELECT * FROM attendance_request
        WHERE employeeId = :employeeId
          AND attendanceDate = :attendanceDate
          AND requestStatus NOT IN ('REJECTED', 'CANCELLED')
        LIMIT 1
        """
    )
    suspend fun getActiveRequestForDate(
        employeeId: String,
        attendanceDate: String
    ): AttendanceRequestEntity?

    @Query("SELECT * FROM attendance_request WHERE syncStatus = :syncStatus")
    suspend fun getRequestsBySyncStatus(
        syncStatus: SyncStatus = SyncStatus.PENDING
    ): List<AttendanceRequestEntity>

    @Query(
        """
        UPDATE attendance_request
        SET erpNextId = :erpNextId,
            syncStatus = :syncStatus,
            updatedAt = :updatedAt
        WHERE id = :id
        """
    )
    suspend fun updateSyncDetails(
        id: Long,
        erpNextId: String,
        syncStatus: SyncStatus,
        updatedAt: Long
    )

    @Query(
        """
        UPDATE attendance_request
        SET requestStatus = :requestStatus,
            updatedAt = :updatedAt
        WHERE erpNextId = :erpNextId
        """
    )
    suspend fun updateRequestStatus(
        erpNextId: String,
        requestStatus: AttendanceRequestStatus,
        updatedAt: Long
    )
}
