package com.example.workpulse.data.repository

import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.core.worker.SyncScheduler
import com.example.workpulse.data.local.dao.AttendanceRequestDao
import com.example.workpulse.data.local.entity.AttendanceRequestEntity
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.data.remote.AttendanceRequestApi
import com.example.workpulse.data.remote.dto.request.AttendanceRequestRequest
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class AttendanceRequestRepository @Inject constructor(
    private val attendanceRequestDao: AttendanceRequestDao,
    private val sessionManager: SessionManager,
    private val attendanceRequestApi : AttendanceRequestApi,
    private val syncScheduler: SyncScheduler
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
        requestType: AttendanceRequestType?,
        fromDate: Long?,
        toDate: Long?,
        location: String,
        onHolidayInclude: Boolean,
        reason: String,
        sourceAttendanceId: Long? = null
    ): Result<Long> {
        if(fromDate == null || toDate == null){
            return Result.failure(
                IllegalArgumentException("From Date and To Date are required")
            )
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
        if(requestType == null){
            return Result.failure(IllegalArgumentException("Request Type is Required"))
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

            val localId = attendanceRequestDao.insertAttendanceRequest(
                AttendanceRequestEntity(
                    employeeId = employeeId,
                    attendanceDate = attendanceDate,
                    fromDate = fromDate,
                    toDate = toDate,
                    reason = reason,
                    requestType = requestType,
                    includeHolidays = onHolidayInclude,
                    location = location,
                    sourceAttendanceId = sourceAttendanceId
                )
            )
            syncScheduler.scheduleAttendanceRequestSync()

            localId



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

    private fun formatDate(time: Long): String {

        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date(time))

    }




    suspend fun syncPendingAttendanceRequests() {

        val pendingRequests = getPendingAttendanceRequests()

        if (pendingRequests.isEmpty()) return

        for (request in pendingRequests) {

            try {

                val apiRequest = AttendanceRequestRequest(

                    employee = request.employeeId,

                    fromDate = formatDate(request.fromDate!!),

                    toDate = formatDate(request.toDate!!),

                    requestType = request.requestType.displayName,

                    includeHolidays = request.includeHolidays,
//                        if (request.includeHolidays) 1 else 0,

                    reason = request.reason

                )

                val response =
                    attendanceRequestApi.createAttendanceRequest(apiRequest)

                if (response.isSuccessful) {

                    val erpNextId =
                        response.body()?.data?.name ?: continue

                    markRequestSynced(

                        localId = request.id,

                        erpNextId = erpNextId

                    )

                }

            } catch (e: java.io.IOException) {

                // No internet.
                throw e

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }
}
