package com.example.workpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workpulse.data.local.entity.ApplicationStatus
import com.example.workpulse.data.local.entity.CompOffApplicationEntity
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CompOffApplicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComposeOffApplication(
        compOffApplication : CompOffApplicationEntity
    )

    @Update
    suspend fun updateComposeOffApplication(
        compOffApplication : CompOffApplicationEntity
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
    ): CompOffApplicationEntity?



    @Query("""
        SELECT *
        FROM compOff_application
        WHERE syncStatus = :syncStatus
    """)
    fun getPendingCompOffApplication(
        syncStatus: SyncStatus = SyncStatus.PENDING
    ): List<CompOffApplicationEntity>

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
    ): CompOffApplicationEntity?


    @Query("""
    UPDATE compOff_application
    SET syncStatus = :syncStatus,
    compOffApplicationStatus = :compOffApplicationStatus,
        syncErrorMessage = :errorMessage
    WHERE id = :id
""")
    suspend fun updateSyncFailure(
        id: Long,
        syncStatus: SyncStatus,
        compOffApplicationStatus: ApplicationStatus,
        errorMessage: String
    )


    @Query("""
        SELECT *
        FROM compOff_application
        ORDER BY fromDate DESC
    """)
    fun getAllCompOffApplications() : Flow<List<CompOffApplicationEntity>>


}