package com.example.workpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workpulse.data.local.entity.ComposeOffRequestEntity
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ComposeOffRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComposeOffApplication(
        compOffApplication : ComposeOffRequestEntity
    )

    @Update
    suspend fun updateComposeOffApplication(
        compOffApplication : ComposeOffRequestEntity
    )

    @Query("""
        SELECT *
        FROM compOff_application
        WHERE employeeId = :employeeId
        AND compOffApplicationStatus != 'REJECTED'
        AND (
        fromDate <= :toDate
        AND toDate >= :fromDate
        ) LIMIT 1
    """)
    suspend fun getOverlappingLeaveApplication(
        employeeId: String,
        fromDate: Long?,
        toDate: Long?
    ): ComposeOffRequestEntity?



    @Query("""
        SELECT *
        FROM compOff_application
        WHERE syncStatus = :syncStatus
    """)
    suspend fun getPendingCompOffApplication(
        syncStatus: SyncStatus = SyncStatus.PENDING
    ): Flow<List<ComposeOffRequestEntity>>

    @Query("""
        UPDATE compoff_application
        SET erpNextId = :erpNextId,
        syncStatus = :syncStatus
        WHERE id = :id
    """)
    suspend fun updateSyncDetails(
        id : Long,
        erpNextId : String,
        syncStatus: SyncStatus,
    )


    @Query("""
        SELECT *
        FROM compOff_application
        WHERE erpNextId = :erpNextId
        LIMIT 1
    """)
    suspend fun getByErpNextId(
        erpNextId: String
    ): ComposeOffRequestEntity?


    @Query("""
        SELECT *
        FROM compOff_application
        ORDER BY fromDate DESC
    """)
    fun getAllCompOffApplications() : Flow<List<ComposeOffRequestEntity>>


}