package com.example.workpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workpulse.data.local.entity.LeaveApplicationEntity
import com.example.workpulse.data.local.entity.LeaveApplicationStatus
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

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
        UPDATE leave_application
        SET erpNextId = :erpNextId,
        syncStatus = :syncStatus,
        updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun updateSyncDetails(
        id: Long,
        erpNextId : String,
        syncStatus: SyncStatus,
        updatedAt: Long
    )



    @Query("""
        SELECT *
        FROM  leave_application
        WHERE erpNextId = :erpNextId
        LIMIT 1
    """)
    suspend fun getByErpNextId(
        erpNextId: String
    ) : LeaveApplicationEntity?


    @Query("""
UPDATE leave_application
SET
    applicationStatus = :status,
    updatedAt = :updatedAt
WHERE erpNextId = :erpNextId
""")
    suspend fun updateApplicationStatus(

        erpNextId: String,

        status: LeaveApplicationStatus,

        updatedAt: Long

    )

    @Query("""
        SELECT *
        FROM leave_application
        WHERE id = :id
    """)
    suspend fun getLeaveApplication(
        id: Long
    ) : LeaveApplicationEntity?



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        leaveApplications: List<LeaveApplicationEntity>
    )


    @Query("""
        SELECT * 
        FROM leave_application
        ORDER BY fromDate DESC
    """)
    fun getAllLeaveApplications(): Flow<List<LeaveApplicationEntity>>

}