package com.example.workpulse.data.repository

import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.data.local.dao.AttendanceRequestDao
import com.example.workpulse.data.local.entity.AttendanceRequestEntity
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
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
        reason: String,
        requestedPunchInTime: Long? = null,
        requestedPunchOutTime: Long? = null,
        sourceAttendanceId: Long? = null
    ): Result<Long> {
        if (attendanceDate.isBlank()) {
            return Result.failure(IllegalArgumentException("Attendance date is required."))
        }
        if (reason.isBlank()) {
            return Result.failure(IllegalArgumentException("A reason is required."))
        }
        if (
            requestedPunchInTime != null &&
            requestedPunchOutTime != null &&
            requestedPunchOutTime < requestedPunchInTime
        ) {
            return Result.failure(
                IllegalArgumentException("Punch-out time cannot be before punch-in time.")
            )
        }

        return runCatching {
            val employeeId = sessionManager.getEmployeeId()
            require(employeeId.isNotBlank()) { "No logged-in employee was found." }

            check(
                attendanceRequestDao.getActiveRequestForDate(
                    employeeId = employeeId,
                    attendanceDate = attendanceDate
                ) == null
            ) { "An active attendance request already exists for this date." }

            attendanceRequestDao.insertAttendanceRequest(
                AttendanceRequestEntity(
                    employeeId = employeeId,
                    attendanceDate = attendanceDate,
                    requestedPunchInTime = requestedPunchInTime,
                    requestedPunchOutTime = requestedPunchOutTime,
                    reason = reason.trim(),
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
