package com.example.workpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workpulse.data.local.entity.LeaveApplicationEntity
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus

@Dao
interface LeaveApplicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaveApplication(
        leaveApplication: LeaveApplicationEntity
    )

    @Update
    suspend fun updateLeaveApplication(
        leaveApplication: LeaveApplicationEntity
    )

    @Query("""
        SELECT *
        FROM leave_application
        WHERE employeeId = :employeeId
        AND applicationStatus != 'REJECTED'
        AND (
        fromDate <= :toDate
        AND toDate >= :fromDate
        ) LIMIT 1
    """)
    suspend fun getOverlappingLeaveApplication(
        employeeId: String,
        fromDate: Long?,
        toDate: Long?
    ): LeaveApplicationEntity?


    @Query("""
        SELECT *
        FROM leave_application
        WHERE syncStatus = :syncStatus
    """)
    suspend fun getPendingLeaveApplication(
        syncStatus: SyncStatus = SyncStatus.PENDING
    ): List<LeaveApplicationEntity>


    @Query("""
        UPDATE leave_application
        SET syncStatus = :syncStatus,
        updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun updateSyncStatus(
        id: Long,
        syncStatus: SyncStatus,
        updatedAt : Long
    )

    @Query("""
        SELECT *
        FROM leave_application
        WHERE id = :id
    """)
    suspend fun getLeaveApplication(
        id: Long
    ) : LeaveApplicationEntity?


    @Query("""
        SELECT * 
        FROM leave_application
        ORDER BY createdAt DESC
    """)
    suspend fun getLeaveApplications(): List<LeaveApplicationEntity>

}