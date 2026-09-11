package com.example.workpulse.data.repository

import android.companion.DeviceId
import android.util.Log
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.core.location.LocationManager
import com.example.workpulse.core.location.ReverseGeocoder
import com.example.workpulse.core.worker.SyncScheduler
import com.example.workpulse.data.local.dao.EmployeeDao
import com.example.workpulse.data.local.entity.LocationStatus
import com.example.workpulse.data.remote.AttendanceApi
import com.example.workpulse.feature.attendance.data.local.dao.AttendanceDao
import com.example.workpulse.feature.attendance.data.local.entity.AttendanceEntity
import com.example.workpulse.feature.attendance.data.local.entity.AttendanceStatus
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import com.example.workpulse.data.remote.dto.request.EmployeeCheckinRequest
import com.example.workpulse.feature.home.domain.model.AttendanceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Singleton
class AttendanceRepository @Inject constructor(
    private val attendanceDao: AttendanceDao,
    private val employeeDao: EmployeeDao,
    private val sessionManager: SessionManager,
    private val locationManager: LocationManager,
    private val attendanceApi: AttendanceApi,
    private val syncScheduler: SyncScheduler,
    private val reverseGeocoder: ReverseGeocoder
){
    suspend fun getTodayAttendance(): AttendanceEntity? {

        val employee = getCurrentEmployee() ?: return null

        return attendanceDao
            .getTodayAttendance(
                employee.employeeId,
                getTodayDate()
            )
            .firstOrNull()
    }

    suspend fun fetchEmployeeCheckins(employeeId: String) {

        try {

            val fields =
                """["name","employee","time","log_type","device_id"]"""

            val filters =
                """[["employee","=","$employeeId"]]"""

            val response = attendanceApi.getEmployeeCheckins(
                fields = fields,
                filters = filters
            )

            if (!response.isSuccessful) {

                Log.e(
                    "AttendanceHistory",
                    "API failed: ${response.code()} ${response.message()}"
                )

                Log.e(
                    "AttendanceHistory",
                    "Error: ${response.errorBody()?.string()}"
                )

                return
            }

            val checkins = response.body()?.data.orEmpty()

            Log.d(
                "AttendanceHistory",
                "Fetched ${checkins.size} employee checkIns"
            )

            if (checkins.isEmpty()) {
                Log.d(
                    "AttendanceHistory",
                    "No check-ins found"
                )
                return
            }

            // ------------------------------------------------------------
            // Parse and sort check-ins
            // ------------------------------------------------------------

            val dateTimeFormatter =
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                )

            val sortedCheckins = checkins
                .mapNotNull { checkin ->

                    try {

                        val date = dateTimeFormatter.parse(checkin.time)

                        if (date == null) {

                            Log.e(
                                "AttendanceCalculation",
                                "Could not parse time: ${checkin.time}"
                            )

                            null

                        } else {

                            ParsedCheckin(
                                name = checkin.name,
                                employee = checkin.employee,
                                time = checkin.time,
                                date = date,
                                logType = checkin.logType?.uppercase(),
                                deviceId = checkin.deviceId
                            )
                        }

                    } catch (e: Exception) {

                        Log.e(
                            "AttendanceCalculation",
                            "Invalid time: ${checkin.time}",
                            e
                        )

                        null
                    }

                }
                .sortedBy { it.date }

            // ------------------------------------------------------------
            // Group by employee + date
            // ------------------------------------------------------------

            val groupedCheckins =
                sortedCheckins.groupBy { checkin ->

                    val attendanceDate =
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(checkin.date)

                    "${checkin.employee}|$attendanceDate"
                }

            // ------------------------------------------------------------
            // Calculate each day's attendance
            // ------------------------------------------------------------

            groupedCheckins.forEach { (groupKey, dailyCheckins) ->

                val employee =
                    dailyCheckins.first().employee

                val attendanceDate =
                    groupKey.substringAfter("|")

                var openInTime: Date? = null

                var firstInTime: Date? = null
                var lastOutTime: Date? = null

                var totalWorkingSeconds = 0L
                var completedPairs = 0


                Log.d(
                    "AttendanceCalculation",
                    "========================================"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Employee = $employee"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Date = $attendanceDate"
                )

                // --------------------------------------------------------
                // Process IN / OUT sequence
                // --------------------------------------------------------

                dailyCheckins
                    .sortedBy { it.date }
                    .forEach { checkin ->

                        when (checkin.logType) {

                            "IN" -> {

                                if (openInTime == null) {

                                    openInTime = checkin.date

                                    if (firstInTime == null) {
                                        firstInTime = checkin.date
                                    }

                                    Log.d(
                                        "AttendanceCalculation",
                                        "IN  = ${checkin.time}"
                                    )

                                } else {

                                    Log.w(
                                        "AttendanceCalculation",
                                        "Duplicate IN ignored = ${checkin.time}"
                                    )
                                }
                            }

                            "OUT" -> {

                                if (openInTime != null) {

                                    val workedMilliseconds =
                                        checkin.date.time - openInTime!!.time

                                    val workedSeconds =
                                        workedMilliseconds / 1000

                                    if (workedSeconds >= 0) {

                                        totalWorkingSeconds += workedSeconds
                                        completedPairs++

                                        lastOutTime = checkin.date

                                        Log.d(
                                            "AttendanceCalculation",
                                            "OUT = ${checkin.time}"
                                        )

                                        Log.d(
                                            "AttendanceCalculation",
                                            "Session = ${formatDuration(workedSeconds)}"
                                        )

                                        openInTime = null

                                    } else {

                                        Log.w(
                                            "AttendanceCalculation",
                                            "Invalid OUT before IN = ${checkin.time}"
                                        )
                                    }

                                } else {

                                    Log.w(
                                        "AttendanceCalculation",
                                        "Unmatched OUT ignored = ${checkin.time}"
                                    )
                                }
                            }

                            else -> {

                                Log.w(
                                    "AttendanceCalculation",
                                    "Unknown log type = ${checkin.logType}"
                                )
                            }
                        }
                    }

                // --------------------------------------------------------
                // Determine status
                // --------------------------------------------------------

                val status =
                    when {

                        openInTime != null ->
                            AttendanceStatus.PUNCHED_IN

                        completedPairs > 0 ->
                            AttendanceStatus.PUNCHED_OUT

                        else ->
                            AttendanceStatus.NOT_PUNCHED_IN
                    }

                val sourceDeviceInfo =
                    dailyCheckins
                        .mapNotNull { it.deviceId }
                        .firstOrNull { it.isNotBlank() }

                val parsedDeviceInfo =
                    parseDeviceInfo(sourceDeviceInfo)

                val punchInMillis = firstInTime?.time
                val punchOutMillis = lastOutTime?.time

                val existingAttendance =
                    attendanceDao.getAttendanceForDate(
                        employeeId = employee,
                        attendanceDate = attendanceDate
                    )

                val attendance = if (existingAttendance != null) {

                    existingAttendance.copy(
                        punchInTime = punchInMillis,
                        punchOutTime = punchOutMillis,
                        workingSeconds = totalWorkingSeconds,
                        status = status,

                        punchInSyncStatus = SyncStatus.SYNCED,

                        punchOutSyncStatus =
                            if (punchOutMillis != null) {
                                SyncStatus.SYNCED
                            } else {
                                SyncStatus.NOT_REQUIRED
                            },

                        deviceId = parsedDeviceInfo.deviceId,
                        location = parsedDeviceInfo.location,

                        updatedAt = System.currentTimeMillis()
                    )

                } else {

                    AttendanceEntity(
                        employeeId = employee,
                        attendanceDate = attendanceDate,
                        punchInTime = punchInMillis,
                        punchOutTime = punchOutMillis,
                        workingSeconds = totalWorkingSeconds,
                        status = status,

                        punchInSyncStatus = SyncStatus.SYNCED,

                        punchOutSyncStatus =
                            if (punchOutMillis != null) {
                                SyncStatus.SYNCED
                            } else {
                                SyncStatus.NOT_REQUIRED
                            },

                        deviceId = parsedDeviceInfo.deviceId,
                        location = parsedDeviceInfo.location
                    )
                }


                if (existingAttendance != null) {
                    attendanceDao.updateAttendance(attendance)
                } else {
                    attendanceDao.insertAttendance(attendance)
                }

                // --------------------------------------------------------
                // Final result
                // --------------------------------------------------------

                Log.d(
                    "AttendanceCalculation",
                    "----------------------------------------"
                )

                Log.d(
                    "AttendanceCalculation",
                    "FINAL RESULT"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Employee      = $employee"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Date          = $attendanceDate"
                )

                Log.d(
                    "AttendanceCalculation",
                    "First IN      = ${
                        firstInTime?.let {
                            dateTimeFormatter.format(it)
                        } ?: "null"
                    }"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Last OUT      = ${
                        lastOutTime?.let {
                            dateTimeFormatter.format(it)
                        } ?: "null"
                    }"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Pairs         = $completedPairs"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Working Sec   = $totalWorkingSeconds"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Working Time  = ${formatDuration(totalWorkingSeconds)}"
                )

                Log.d(
                    "AttendanceCalculation",
                    "Status        = $status"
                )

                if (openInTime != null) {

                    Log.d(
                        "AttendanceCalculation",
                        "Open IN       = ${
                            dateTimeFormatter.format(openInTime!!)
                        }"
                    )
                }

                Log.d(
                    "AttendanceCalculation",
                    "========================================"
                )
            }

        } catch (e: Exception) {

            Log.e(
                "AttendanceCalculation",
                "Calculation failed",
                e
            )
        }
    }



    fun observeTodayAttendance(): Flow<AttendanceEntity?> = flow {

        val employee = employeeDao.getEmployeeOnce()

        if (employee == null) {
            emit(null)
            return@flow
        }

        emitAll(
            attendanceDao.getTodayAttendance(
                employeeId = employee.employeeId,
                attendanceDate = getTodayDate()
            )
        )
    }

    /**
     * Attendance history
     */
    fun getAttendanceHistory(
        employeeId: String
    ): Flow<List<AttendanceEntity>> {
        return attendanceDao.getAttendanceHistory(employeeId)
    }

    /**
     * Punch In
     */
    suspend fun punchIn() : AttendanceResult {
        val employee = employeeDao.getEmployeeOnce()?: return AttendanceResult.Error("Employee not Found")

        if (!locationManager.hasLocationPermission()) {
            return AttendanceResult.PermissionRequired
        }

        if (!locationManager.isLocationEnabled()) {
            return AttendanceResult.GpsDisabled
        }

        val attendanceDate = getTodayDate()

        val existingAttendance = attendanceDao.getTodayAttendance(
            employeeId = employee.employeeId,
            attendanceDate = attendanceDate
        ).firstOrNull()

        if (existingAttendance != null) {
            return AttendanceResult.AlreadyPunchedIn
        }

        val location = locationManager.getCurrentLocation()

        if (location == null) {
            return AttendanceResult.LocationUnavailable
        }

        val appUuid = sessionManager.getOrCreateAppUuid()

        val deviceInfo = buildString {

            append("Device=")
            append(appUuid)

            location.latitude?.let {

                append(" | Lat=")
                append(it)

            }

            location.longitude?.let {

                append(" | Lng=")
                append(it)

            }

        }


        val attendance = AttendanceEntity(
            employeeId = employee.employeeId,
            attendanceDate = attendanceDate,
            punchInTime = System.currentTimeMillis(),

            latitude = location.latitude,
            logitude = location.longitude,
            accuracy = location.accuracy,
            deviceId = deviceInfo,
            locationStatus = LocationStatus.SUCCESS,

            status = AttendanceStatus.PUNCHED_IN,

            punchInSyncStatus = SyncStatus.PENDING,
            punchOutSyncStatus = SyncStatus.NOT_REQUIRED
        )

        attendanceDao.insertAttendance(attendance)
        syncScheduler.scheduleAttendanceSync()


        return AttendanceResult.Success
    }

    /**
     * Punch Out
     */
    suspend fun punchOut() : AttendanceResult{
        val employee = employeeDao.getEmployeeOnce()
            ?: return AttendanceResult.Error("Employee not found")



        if (!locationManager.hasLocationPermission()) {
            return AttendanceResult.PermissionRequired
        }
        if(!locationManager.isLocationEnabled()){
            return AttendanceResult.GpsDisabled
        }

        val attendance = getTodayAttendance() ?: return AttendanceResult.Error("Today's attendance not found")




        val location = locationManager.getCurrentLocation()
        if(location == null){
            return AttendanceResult.LocationUnavailable
        }

        val appUuid = sessionManager.getOrCreateAppUuid()







        val deviceInfo = buildString {

            append("Device=")
            append(appUuid)

            attendance.latitude?.let {

                append(" | Lat=")
                append(it)

            }

            attendance.logitude?.let {

                append(" | Lng=")
                append(it)

            }

        }

        val now = System.currentTimeMillis()
        val workingSeconds =
            (now - (attendance.punchInTime ?: now)) / 1000

        attendanceDao.updateAttendance(
            attendance.copy(
                punchOutTime = now,
                workingSeconds = workingSeconds,
                latitude = location.latitude,
                logitude = location.longitude,
                accuracy = location.accuracy,
                deviceId = deviceInfo,
                locationStatus = LocationStatus.SUCCESS,
                status = AttendanceStatus.PUNCHED_OUT,
                punchOutSyncStatus = SyncStatus.PENDING,
                updatedAt = now
            )
        )

        syncScheduler.scheduleAttendanceSync()
        return AttendanceResult.Success
    }
//
//    /**
//     * Update sync status
//     */
//
//
//    /**
//     * Pending attendance
//     */
//    suspend fun getPendingAttendance(): List<AttendanceEntity> {
//        return attendanceDao.getPendingAttendance()
//    }
//
//    /**
//     * Clear attendance of an employee
//     */
//    suspend fun deleteAttendanceByEmployee(
//        employeeId: String
//    ) {
//        attendanceDao.deleteAttendanceByEmployee(employeeId)
//    }


    private suspend fun getCurrentEmployee() =
        employeeDao.getEmployeeOnce()

    private fun getTodayDate(): String =
        LocalDate.now().toString()


    suspend fun syncPendingAttendance() {

        val pendingAttendance = attendanceDao.getPendingAttendance()

        if (pendingAttendance.isEmpty()) return

        for (attendance in pendingAttendance) {

            try {

                //Updating a project to convert the longitude and latitude into location:

                var updatedAttendance = attendance

                val locationText =

                    if (
                        attendance.latitude != null &&
                        attendance.logitude != null
                    ) {

                        reverseGeocoder.getAddress(
                            attendance.latitude,
                            attendance.logitude
                        )

                    } else {

                        ""

                    }

                val deviceInfo = buildString {

                    if (locationText.isNotBlank()) {

                        append("Location=")
                        append(locationText)

                    } else {

                        attendance.latitude?.let {

                            append(" | Lat=")
                            append(it)

                        }

                        attendance.logitude?.let {

                            append(" | Lng=")
                            append(it)

                        }

                    }

                    append(" | Device=")
                    append(updatedAttendance.deviceId?.substringBefore(" |") ?: "")

                }

                updatedAttendance = updatedAttendance.copy(

                    deviceId = deviceInfo,
                    location = locationText,

                    updatedAt = System.currentTimeMillis()

                )

                attendanceDao.updateAttendance(updatedAttendance)



                // -------------------------------
                // Sync Punch In
                // -------------------------------
                if (attendance.punchInSyncStatus == SyncStatus.PENDING) {

                    val punchInTime = attendance.punchInTime

                    if (punchInTime != null) {
                        Log.d("Attendance", "Room PunchIn = ${attendance.punchInTime}")
                        Log.d("Attendance", "Formatted = ${formatDateTime(attendance.punchInTime!!)}")

                        val punchInRequest = EmployeeCheckinRequest(
                            employee = attendance.employeeId,
                            time = formatDateTime(punchInTime),
                            logType = "IN",
                            deviceId = updatedAttendance.deviceId,
                            latitude = attendance.latitude,
                            longitude = attendance.logitude
                        )

                        val response =
                            attendanceApi.createEmployeeCheckin(punchInRequest)

                        if (response.isSuccessful) {

                            updatedAttendance = updatedAttendance.copy(
                                punchInSyncStatus = SyncStatus.SYNCED,
                                location = updatedAttendance.location,
                                updatedAt = System.currentTimeMillis()
                            )


                            attendanceDao.updateAttendance(updatedAttendance)

                        } else {

                            updatedAttendance = updatedAttendance.copy(
                                punchInSyncStatus = SyncStatus.FAILED,
                                updatedAt = System.currentTimeMillis()
                            )

                            attendanceDao.updateAttendance(updatedAttendance)
                        }
                    }
                }

                // -------------------------------
                // Sync Punch Out
                // -------------------------------
                if (
                    updatedAttendance.punchOutTime != null &&
                    updatedAttendance.punchOutSyncStatus == SyncStatus.PENDING
                ) {

                    val punchOutRequest = EmployeeCheckinRequest(
                        employee = updatedAttendance.employeeId,
                        time = formatDateTime(updatedAttendance.punchOutTime!!),
                        logType = "OUT",
                        deviceId = updatedAttendance.deviceId,
                        latitude = updatedAttendance.latitude,
                        longitude = updatedAttendance.logitude
                    )

                    val response =
                        attendanceApi.createEmployeeCheckin(punchOutRequest)

                    if (response.isSuccessful) {

                        updatedAttendance = updatedAttendance.copy(
                            punchOutSyncStatus = SyncStatus.SYNCED,
                            location = updatedAttendance.location,
                            updatedAt = System.currentTimeMillis()
                        )

                        attendanceDao.updateAttendance(updatedAttendance)

                    } else {

                        updatedAttendance = updatedAttendance.copy(
                            punchOutSyncStatus = SyncStatus.FAILED,
                            updatedAt = System.currentTimeMillis()
                        )

                        attendanceDao.updateAttendance(updatedAttendance)
                    }
                }

            } catch (e: java.io.IOException) {

                // No internet / server unavailable.
                // Keep status as PENDING so WorkManager retries later.
                throw e

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }


    private fun formatDuration(totalSeconds : Long) : String{
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return "${hours}h ${minutes}m ${seconds}s"
    }
    private fun formatDateTime(timeInMillis: Long): String {

        return SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date(timeInMillis))
    }


    private data class ParsedCheckin(
        val name: String,
        val employee : String,
        val time : String,
        val date: Date,
        val logType : String?,
        val deviceId : String?
    )



    private data class ParsedDeviceInfo(
        val location: String,
        val deviceId: String
    )

    private fun parseDeviceInfo(deviceInfo: String?): ParsedDeviceInfo {

        if (deviceInfo.isNullOrBlank()) {
            return ParsedDeviceInfo(
                location = "",
                deviceId = ""
            )
        }

        val parts = deviceInfo.split("|")

        var location = ""
        var deviceId = ""

        parts.forEach { part ->

            val trimmed = part.trim()

            when {
                trimmed.startsWith("Location=", ignoreCase = true) -> {
                    location = trimmed
                        .substringAfter("=", "")
                        .trim()
                }

                trimmed.startsWith("Device=", ignoreCase = true) -> {
                    deviceId = trimmed
                        .substringAfter("=", "")
                        .removePrefix("Device=")
                        .trim()
                }
            }
        }

        return ParsedDeviceInfo(
            location = location,
            deviceId = deviceId
        )
    }

}

