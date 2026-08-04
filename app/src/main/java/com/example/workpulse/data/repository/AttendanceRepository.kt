package com.example.workpulse.data.repository

import android.util.Log
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.core.location.LocationManager
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
    private val syncScheduler: SyncScheduler
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

    /**
     * Update sync status
     */


    /**
     * Pending attendance
     */
    suspend fun getPendingAttendance(): List<AttendanceEntity> {
        return attendanceDao.getPendingAttendance()
    }

    /**
     * Clear attendance of an employee
     */
    suspend fun deleteAttendanceByEmployee(
        employeeId: String
    ) {
        attendanceDao.deleteAttendanceByEmployee(employeeId)
    }


    private suspend fun getCurrentEmployee() =
        employeeDao.getEmployeeOnce()

    private fun getTodayDate(): String =
        LocalDate.now().toString()


    suspend fun syncPendingAttendance() {

        val pendingAttendance = attendanceDao.getPendingAttendance()

        if (pendingAttendance.isEmpty()) return

        for (attendance in pendingAttendance) {

            try {

                var updatedAttendance = attendance

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
                            deviceId = attendance.deviceId,
                            latitude = attendance.latitude,
                            longitude = attendance.logitude
                        )

                        val response =
                            attendanceApi.createEmployeeCheckin(punchInRequest)

                        if (response.isSuccessful) {

                            updatedAttendance = updatedAttendance.copy(
                                punchInSyncStatus = SyncStatus.SYNCED,
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
    private fun formatDateTime(timeInMillis: Long): String {

        return SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date(timeInMillis))
    }




}