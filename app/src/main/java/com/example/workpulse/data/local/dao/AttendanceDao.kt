package com.example.workpulse.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workpulse.feature.attendance.data.local.entity.AttendanceEntity
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    /**
     * Insert a new attendance record
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    /**
     * Update an existing attendance record
     */
    @Update
    suspend fun updateAttendance(attendance: AttendanceEntity)

    /**
     * Get today's attendance for an employee
     */
    @Query(
        """
        SELECT * FROM attendance
        WHERE employeeId = :employeeId
        AND attendanceDate = :attendanceDate
        LIMIT 1
        """
    )
    fun getTodayAttendance(
        employeeId: String,
        attendanceDate: String
    ): Flow<AttendanceEntity?>

    /**
     * Attendance history
     */
    @Query(
        """
        SELECT * FROM attendance
        WHERE employeeId = :employeeId
        ORDER BY attendanceDate DESC
        """
    )
    fun getAttendanceHistory(
        employeeId: String
    ): Flow<List<AttendanceEntity>>

    /**
     * Pending records waiting for synchronization
     */
    @Query("""
    SELECT *
    FROM attendance
    WHERE
        punchInSyncStatus = 'PENDING'
        OR
        (
            punchOutTime IS NOT NULL
            AND punchOutSyncStatus = 'PENDING'
        )
    """)
    suspend fun getPendingAttendance(): List<AttendanceEntity>

    /**
     * Delete all attendance of an employee
     * (used during logout)
     */
    @Query(
        """
        DELETE FROM attendance
        WHERE employeeId = :employeeId
        """
    )
    suspend fun deleteAttendanceByEmployee(
        employeeId: String
    )


    /**
     * Delete everything
     * (mainly useful during development/testing)
     */
    @Query("DELETE FROM attendance")
    suspend fun deleteAllAttendance()
}