package com.example.workpulse.data.repository

import android.util.Log
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
    private val syncScheduler: SyncScheduler,
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

        var uploadSuccessful = false

        for (request in pendingRequests) {

            try {

                val apiRequest = AttendanceRequestRequest(

                    employee = request.employeeId,

                    fromDate = formatDate(request.fromDate!!),

                    toDate = formatDate(request.toDate!!),

                    requestType = request.requestType.displayName,

                    includeHolidays = request.includeHolidays,

                    reason = request.reason

                )

                val response =
                    attendanceRequestApi.createAttendanceRequest(apiRequest)

                if (response.isSuccessful) {

                    val erpNextId =
                        response.body()?.data?.name

                    if (erpNextId != null) {

                        markRequestSynced(
                            localId = request.id,
                            erpNextId = erpNextId
                        )

                        uploadSuccessful = true

                    }
                }

            } catch (e: java.io.IOException) {

                // No internet.
                throw e

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

        /*
         * At least one request was successfully uploaded.
         * Now download the latest ERPNext records and update Room.
         */
//        if (uploadSuccessful) {
//
//            syncLatestAttendanceRequests()
//
//        }
    }



    suspend fun syncLatestAttendanceRequests() {

        val employeeId =
            sessionManager.getEmployeeId()

        if (employeeId.isBlank()) {
            return
        }

        val filters = """
        [
            ["Attendance Request", "employee", "=", "$employeeId"]
        ]
    """.trimIndent()

        val fields = """
        [
            "name",
            "employee",
            "from_date",
            "to_date",
            "include_holidays",
            "reason",
            "explanation",
            "docstatus"
        ]
    """.trimIndent()

        val response =
            attendanceRequestApi.getAttendanceRequests(

                filters = filters,

                fields = fields,

                orderBy = "creation desc",

                limit = 20

            )

        if (!response.isSuccessful) {

            throw Exception(
                "Failed to fetch attendance requests: ${response.code()}"
            )
        }

        val serverRequests =
            response.body()?.data
                ?: emptyList()

        for (serverRequest in serverRequests) {
            Log.d(
                "AttendanceRequestSync",
                "ERPNext Request: $serverRequest"
            )

            Log.d(
                "AttendanceRequestStatus",
                "ERPNext: ${serverRequest.name} " +
                        "docStatus=${serverRequest.docStatus} "

            )

            val existingRequest =
                attendanceRequestDao
                    .getRequestByErpNextId(
                        serverRequest.name
                    )


            Log.d(
                "AttendanceRequestSync",
                "Local lookup: erpNextId=${serverRequest.name}, " +
                        "localId=${existingRequest?.id}, " +
                        "localStatus=${existingRequest?.requestStatus}"
            )

            val fromDate =
                parseErpNextDate(
                    serverRequest.fromDate
                )

            val toDate =
                parseErpNextDate(
                    serverRequest.toDate
                )

            val requestType =
                mapRequestType(
                    serverRequest.reason
                )

            val requestStatus =
                mapRequestStatus(
                    serverRequest.docStatus
                )


            if (existingRequest != null) {

                Log.d(
                    "AttendanceRequestSync",
                    "UPDATING: ${existingRequest.id} " +
                            "status $requestStatus"
                )

                attendanceRequestDao.updateAttendanceRequest(

                    existingRequest.copy(

                        employeeId =
                            serverRequest.employee,

                        attendanceDate =
                            serverRequest.fromDate ?: "",

                        fromDate =
                            fromDate,

                        toDate =
                            toDate,

                        reason =
                            serverRequest.explanation ?: "",

                        requestType =
                            requestType,

                        includeHolidays =
                            serverRequest.includeHolidays == 1,

                        requestStatus =
                            requestStatus,

                        syncStatus =
                            SyncStatus.SYNCED,

                        updatedAt =
                            System.currentTimeMillis()
                    )
                )

            } else {

                Log.d(
                    "AttendanceRequestSync",
                    "INSERTING new ERPNext request: ${serverRequest.name}"
                )

                attendanceRequestDao.insertAttendanceRequest(


                    AttendanceRequestEntity(

                        erpNextId = serverRequest.name,

                        employeeId = serverRequest.employee,

                        attendanceDate =
                            serverRequest.fromDate ?: "",

                        fromDate =
                            parseErpNextDate(
                                serverRequest.fromDate
                            ),

                        toDate =
                            parseErpNextDate(
                                serverRequest.toDate
                            ),

                        reason =
                            serverRequest.explanation ?: "",

                        requestType =
                            mapRequestType(
                                requestType.displayName
                            ),

                        includeHolidays =
                            serverRequest.includeHolidays == 1,

                        requestStatus =
                            requestStatus,

                        syncStatus =
                            SyncStatus.SYNCED
                    )
                )
            }
        }
    }
}


//private fun mapRequestType(
//    requestType: String?
//): AttendanceRequestType {
//
//    return when (
//        requestType
//            ?.trim()
//            ?.uppercase()
//    ) {
//
//        "ON_DUTY" ->
//            AttendanceRequestType.ON_DUTY
//
//        "WORK_FROM_HOME" ->
//            AttendanceRequestType.WORK_FROM_HOME
//
//        else ->
//            AttendanceRequestType.ON_DUTY
//    }
//}

private fun mapRequestType(
    reason: String?
): AttendanceRequestType {
    return when (reason?.trim()?.lowercase()) {
        "work from home" ->
            AttendanceRequestType.WORK_FROM_HOME

        "on duty" ->
            AttendanceRequestType.ON_DUTY

        else ->
            AttendanceRequestType.ON_DUTY
    }
}


private fun mapRequestStatus(
    docStatus: Int
): AttendanceRequestStatus {

    return when (docStatus) {

        0 ->
            AttendanceRequestStatus.PENDING

        1 ->
            AttendanceRequestStatus.APPROVED

        2 ->
            AttendanceRequestStatus.CANCELLED

        else ->
            AttendanceRequestStatus.PENDING
    }
}


private fun parseErpNextDate(
    date: String?
): Long? {

    if (date.isNullOrBlank()) {
        return null
    }

    return try {

        java.text.SimpleDateFormat(
            "yyyy-MM-dd",
            java.util.Locale.getDefault()
        )
            .apply {
                timeZone =
                    java.util.TimeZone.getTimeZone("UTC")
            }
            .parse(date)
            ?.time

    } catch (e: Exception) {

        null
    }
}