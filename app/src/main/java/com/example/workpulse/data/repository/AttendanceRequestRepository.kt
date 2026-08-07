package com.example.workpulse.data.repository

import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.data.local.dao.AttendanceRequestDao
import com.example.workpulse.data.local.entity.AttendanceRequestEntity
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class AttendanceRequestRepository @Inject constructor(
    private val attendanceRequestDao: AttendanceRequestDao,
    private val sessionManager: SessionManager
) {

    fun observeAttendanceRequests(): Flow<List<AttendanceRequestEntity>> =
        sessionManager.employeeIdFlow.flatMapLatest { employeeId ->
            if (employeeId.isBlank()) {
                flowOf(emptyList())
            } else {
                attendanceRequestDao.observeAttendanceRequests(employeeId)
            }
        }

    /**
     * Stores a request locally first. A future sync worker can submit every record whose
     * [AttendanceRequestEntity.syncStatus] is PENDING.
     */
    suspend fun createAttendanceRequest(
        attendanceDate: String,
        reason: AttendanceRequestType?,
        fromDate: Long?,
        toDate: Long?,
        location: String,
        sourceAttendanceId: Long? = null
    ): Result<Long> {
        if (reason?.displayName ?: "" == "") {
            return Result.failure(IllegalArgumentException("Request Type is required."))
        }
        if (
            fromDate != null &&
            toDate != null &&
            toDate < fromDate
        ) {
            return Result.failure(
                IllegalArgumentException("From Date should be before To Date")
            )
        }

        return runCatching {
            val employeeId = sessionManager.getEmployeeId()
            require(employeeId.isNotBlank()) { "No logged-in employee was found." }

            if (
                attendanceRequestDao.getActiveRequestForDate(
                    employeeId = employeeId,
                    attendanceDate = attendanceDate
                ) != null
            ) {
                return Result.failure(
                    IllegalArgumentException(
                        "An active attendance request already exists for this date."
                    )
                )
            }

            attendanceRequestDao.insertAttendanceRequest(
                AttendanceRequestEntity(
                    employeeId = employeeId,
                    attendanceDate = attendanceDate,
                    fromDate = fromDate,
                    toDate = toDate,
                    reason = reason?.displayName ?: "",
                    location = location,
                    sourceAttendanceId = sourceAttendanceId
                )
            )
        }
    }

    suspend fun getPendingAttendanceRequests(): List<AttendanceRequestEntity> =
        attendanceRequestDao.getRequestsBySyncStatus()

    suspend fun markRequestSynced(
        localId: Long,
        erpNextId: String
    ) {
        attendanceRequestDao.updateSyncDetails(
            id = localId,
            erpNextId = erpNextId,
            syncStatus = SyncStatus.SYNCED,
            updatedAt = System.currentTimeMillis()
        )
    }

    suspend fun updateRequestStatus(
        erpNextId: String,
        requestStatus: AttendanceRequestStatus
    ) {
        attendanceRequestDao.updateRequestStatus(
            erpNextId = erpNextId,
            requestStatus = requestStatus,
            updatedAt = System.currentTimeMillis()
        )
    }
}
