package com.example.workpulse.data.local.dao

import com.example.workpulse.data.local.entity.LeaveBalanceEntity


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaveBalanceDao {

    @Query("""
        SELECT *
        FROM leave_balance
        WHERE employeeId = :employeeId
    """)
    fun observeLeaveBalance(
        employeeId: String
    ): Flow<LeaveBalanceEntity?>

    @Query("""
    SELECT *
    FROM leave_balance
    WHERE employeeId = :employeeId
    LIMIT 1
""")
    suspend fun getLeaveBalance(
        employeeId: String
    ): LeaveBalanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaveBalance(
        leaveBalance: LeaveBalanceEntity
    )

    @Query("DELETE FROM leave_balance")
    suspend fun clear()

}