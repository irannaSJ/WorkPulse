package com.example.workpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workpulse.data.local.entity.LeaveApplicationEntity
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
        ORDER BY fromDate DESC
    """)
    fun getLeaveApplications(
        employeeId: String
    ): Flow<List<LeaveApplicationEntity>>

}