package com.example.workpulse.data.repository

import android.util.Log
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.core.worker.SyncScheduler
import com.example.workpulse.data.local.dao.EmployeeDao
import com.example.workpulse.data.local.dao.LeaveApplicationDao
import com.example.workpulse.data.local.dao.LeaveBalanceDao
import com.example.workpulse.data.local.entity.LeaveApplicationEntity
import com.example.workpulse.data.local.entity.LeaveApplicationStatus
import com.example.workpulse.data.remote.LeaveApi
import com.example.workpulse.data.remote.dto.request.LeaveApplicationRequest
import com.example.workpulse.data.remote.dto.response.LeaveApplicationData
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import com.example.workpulse.feature.leaveApplication.LeaveApplicationResult
import com.example.workpulse.feature.leaveApplication.LeaveSuggestionUi
import com.example.workpulse.feature.leaveApplication.LeaveType
import com.example.workpulse.feature.leaveApplication.LeaveValidationResult
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class LeaveApplicationRepository @Inject constructor(
    private val leaveApi : LeaveApi,
    private val employeeDao: EmployeeDao,
    private val leaveBalanceDao: LeaveBalanceDao,
    private val sessionManager: SessionManager,
    private val leaveApplicationDao: LeaveApplicationDao,
    private val syncScheduler: SyncScheduler
) {

    suspend fun getAvailableBalance(leaveType : LeaveType) : Double{
        val employeeId = sessionManager.getEmployeeId()?: return 0.0

        val leaveBalance = leaveBalanceDao.getLeaveBalance(employeeId)?: return 0.0

        return when(leaveType) {
            LeaveType.CASUAL -> leaveBalance.casualLeave
            LeaveType.SICK -> leaveBalance.sickLeave
            LeaveType.LEAVE_WITHOUT_PAY -> leaveBalance.leaveWithoutPay
            LeaveType.COMP_OFF -> leaveBalance.compensatoryOff
            LeaveType.PRIVILEGE -> leaveBalance.privilegeLeave
        }
    }




    suspend fun getLeaveSuggestions(
        selectedLeaveType: LeaveType,
        requestedDays: Int
    ): List<LeaveSuggestionUi> {

        val employeeId = sessionManager.getEmployeeId()
            ?: return emptyList()

        val leaveBalance = leaveBalanceDao.getLeaveBalance(employeeId)
            ?: return emptyList()

        val suggestions = mutableListOf<LeaveSuggestionUi>()

        if (
            selectedLeaveType != LeaveType.CASUAL &&
            leaveBalance.casualLeave >= requestedDays
        ) {
            suggestions.add(
                LeaveSuggestionUi(
                    leaveType = LeaveType.CASUAL,
                    availableDays = leaveBalance.casualLeave
                )
            )
        }

        if (
            selectedLeaveType != LeaveType.SICK &&
            leaveBalance.sickLeave >= requestedDays
        ) {
            suggestions.add(
                LeaveSuggestionUi(
                    leaveType = LeaveType.SICK,
                    availableDays = leaveBalance.sickLeave
                )
            )
        }

        if (
            selectedLeaveType != LeaveType.PRIVILEGE &&
            leaveBalance.privilegeLeave >= requestedDays
        ) {
            suggestions.add(
                LeaveSuggestionUi(
                    leaveType = LeaveType.PRIVILEGE,
                    availableDays = leaveBalance.privilegeLeave
                )
            )
        }

        if (
            selectedLeaveType != LeaveType.COMP_OFF &&
            leaveBalance.compensatoryOff >= requestedDays
        ) {
            suggestions.add(
                LeaveSuggestionUi(
                    leaveType = LeaveType.COMP_OFF,
                    availableDays = leaveBalance.compensatoryOff
                )
            )
        }

        if (
            selectedLeaveType != LeaveType.LEAVE_WITHOUT_PAY &&
            leaveBalance.leaveWithoutPay >= requestedDays
        ) {
            suggestions.add(
                LeaveSuggestionUi(
                    leaveType = LeaveType.LEAVE_WITHOUT_PAY,
                    availableDays = leaveBalance.leaveWithoutPay
                )
            )
        }

        return suggestions
    }


    suspend fun validateLeaveApplication(
        leaveType: LeaveType?,
        fromDate : Long?,
        toDate : Long?,
        reason : String,
        requestedDays: Int
    ) : LeaveValidationResult{

        if (leaveType == null){
            return LeaveValidationResult.Error(
                "Please select Leave Type"
            )
        }
        if (fromDate == null){
            return LeaveValidationResult.Error(
                "Please Select the From date"
            )
        }
        if (toDate == null){
            return LeaveValidationResult.Error(
                "Please select the To Date"
            )
        }
        if (reason.isBlank()){
            return LeaveValidationResult.Error(
                "Please Enter the Reason"
            )
        }

        if(reason.trim().length < 10){
            return LeaveValidationResult.Error(
                "The Reason should contain at least 10 characters"
            )
        }

        if(requestedDays <=0){
            return LeaveValidationResult.Error(
                "The Requested Days are less then 0"
            )
        }

        if(fromDate > toDate){
            return LeaveValidationResult.Error(
                "The From Date cannot be after To Date"
            )
        }

        val duplicateValidation = validateDuplicateLeave(fromDate = fromDate, toDate= toDate)

        if(duplicateValidation != LeaveValidationResult.Success){
            return duplicateValidation
        }


        val availableBalance = getAvailableBalance(leaveType)

        if(availableBalance < requestedDays){
            val suggestions = getLeaveSuggestions(
                selectedLeaveType =leaveType,
                requestedDays = requestedDays
            )

            return LeaveValidationResult.InsufficientBalance(availableBalance = availableBalance, suggestions = suggestions)
        }

        val today  = getStartOfToday()
        if(fromDate < today){
            return LeaveValidationResult.Error(
                "From Date cannot be in the past"
            )
        }
        if(toDate < today){
            return LeaveValidationResult.Error(
                "To Date cannot be in the Past"
            )
        }

        if(requestedDays > 30){
            return LeaveValidationResult.Error(
                "Leave Application Cannot exceed 30 days"
            )
        }

        return LeaveValidationResult.Success

    }


    private suspend fun validateDuplicateLeave(
        fromDate: Long?,
        toDate: Long?
    ) : LeaveValidationResult {
        val employeeId = sessionManager.getEmployeeId()
            ?: return LeaveValidationResult.Error(
                "Employee Not Found"
            )

        val existingLeave = leaveApplicationDao.getOverlappingLeaveApplication(
            employeeId = employeeId,
            fromDate = fromDate,
            toDate = toDate
        )

        if(existingLeave != null){
            return LeaveValidationResult.Error(
                "Leave Application Already Exists for the selected dates."
            )
        }


        return LeaveValidationResult.Success
    }





    suspend fun saveLeaveApplication(
        leaveType: LeaveType?,
        fromDate: Long?,
        toDate: Long?,
        requestedDays: Int,
        reason: String
    ): LeaveApplicationResult {

        return try {

            // Get logged in employee
            val employee = employeeDao.getEmployeeOnce()
                ?: return LeaveApplicationResult.Error(
                    "Employee not found."
                )

            // Validate application
            when (
                val validationResult = validateLeaveApplication(
                    leaveType = leaveType,
                    fromDate = fromDate,
                    toDate = toDate,
                    requestedDays = requestedDays,
                    reason = reason
                )
            ) {

                is LeaveValidationResult.Success -> {

                    val currentTime = System.currentTimeMillis()

                    val leaveApplication = LeaveApplicationEntity(

                        employeeId = employee.employeeId,

                        leaveType = leaveType!!.displayName,

                        fromDate = fromDate!!,

                        toDate = toDate!!,

                        requestedDays = requestedDays,

                        description = reason.trim(),

                        applicationStatus = LeaveApplicationStatus.PENDING,

                        syncStatus = SyncStatus.PENDING,
                        leaveApprover = employee.leaveApprover,

                        createdAt = currentTime,

                        updatedAt = currentTime

                    )

                    leaveApplicationDao.insertLeaveApplication(
                        leaveApplication
                    )
                    syncScheduler.scheduleLeaveSync()

                    LeaveApplicationResult.Success
                }

                else -> {

                    LeaveApplicationResult.ValidationFailed(
                        validationResult
                    )

                }
            }

        } catch (exception: Exception) {

            LeaveApplicationResult.Error(
                exception.message ?: "Failed to save leave application."
            )

        }

    }





    private fun getStartOfToday(): Long {

        val calendar = Calendar.getInstance()

        calendar.set(
            Calendar.HOUR_OF_DAY,
            0
        )

        calendar.set(
            Calendar.MINUTE,
            0
        )

        calendar.set(
            Calendar.SECOND,
            0
        )

        calendar.set(
            Calendar.MILLISECOND,
            0
        )

        return calendar.timeInMillis

    }



    suspend fun syncPendingLeaveApplications() {
        updatePendingLeaveApplications()
        downloadLatestLeaveApplications()


    }

    private suspend fun updatePendingLeaveApplications(){
        val pendingApplications =
            leaveApplicationDao.getPendingLeaveApplication()

        for (application in pendingApplications) {

            val request = LeaveApplicationRequest(

                employee = application.employeeId,

                leaveType = application.leaveType,

                fromDate = formatDate(application.fromDate),

                toDate = formatDate(application.toDate),

                description = application.description,
                leaveApprover = application.leaveApprover

            )

            try {

                val response =
                    leaveApi.createLeaveApplication(request)

                if (response.isSuccessful) {
                    val erpNextId = response.body()?.data?.name?:continue

                    leaveApplicationDao.updateSyncDetails(

                        id = application.id,
                        erpNextId = erpNextId,

                        syncStatus = SyncStatus.SYNCED,

                        updatedAt = System.currentTimeMillis()

                    )

                } else {

                    // Keep as PENDING.
                    // Server rejected the request.

                }

            } catch (exception: IOException) {

                // Network unavailable.
                throw exception

            } catch (exception: Exception) {

                exception.printStackTrace()

                // Leave remains PENDING.

            }

        }
    }



    private suspend fun downloadLatestLeaveApplications() {

        val employeeId =
            sessionManager.getEmployeeId()
                ?: return

        val filters = """
        [
            ["employee","=","$employeeId"]
        ]
    """.trimIndent()

        val fields = """
        [
            "name",
            "employee",
            "leave_type",
            "from_date",
            "to_date",
            "status",
            "description"
        ]
    """.trimIndent()

        val response = leaveApi.getLeaveApplications(

            fields = fields,

            filters = filters

        )

        Log.d(
            "LeaveSync",
            "Downloaded ${response.data.size} leave applications"
        )
//
//        response.data.forEach {
//
//            Log.d(
//                "LeaveSync",
//                """
//            -----------------------
//            ERPNext ID : ${it.name}
//            Employee   : ${it.employee}
//            Leave Type : ${it.leaveType}
//            Status     : ${it.status}
//            From Date  : ${it.fromDate}
//            To Date    : ${it.toDate}
//            Descriptions : ${it.description}
//            -----------------------
//            """.trimIndent()
//            )
//
//        }

        response.data.forEach { dto ->

            val entity = dto.toEntity()

            val existing = leaveApplicationDao.getByErpNextId(dto.name)

            if (existing == null) {

                leaveApplicationDao.insertLeaveApplication(entity)

                Log.d(
                    "LeaveSync",
                    "Inserted ${dto.name}"
                )

            } else {

                leaveApplicationDao.updateLeaveApplication(

                    entity.copy(
                        id = existing.id
                    )

                )

                Log.d(
                    "LeaveSync",
                    "Updated ${dto.name}"
                )

            }

        }

    }



    private fun LeaveApplicationData.toEntity(): LeaveApplicationEntity {

        return LeaveApplicationEntity(

            id = 0,

            employeeId = employee,

            erpNextId = name,

            leaveType = leaveType,

            fromDate = parseDate(fromDate),

            toDate = parseDate(toDate),

            requestedDays = calculateRequestedDays(
                parseDate(fromDate),
                parseDate(toDate)
            ),

            description = description.orEmpty(),

            applicationStatus = mapStatus(status),

            syncStatus = SyncStatus.SYNCED,

            createdAt = System.currentTimeMillis(),

            updatedAt = System.currentTimeMillis()

        )
    }




}







private fun formatDate(
    millis: Long
): String {

    return java.text.SimpleDateFormat(
        "yyyy-MM-dd",
        java.util.Locale.getDefault()
    ).format(java.util.Date(millis))

}

private fun mapStatus(
    status: String
): LeaveApplicationStatus {

    return when (status) {

        "Open" ->
            LeaveApplicationStatus.PENDING

        "Approved" ->
            LeaveApplicationStatus.APPROVED

        "Rejected" ->
            LeaveApplicationStatus.REJECTED

        "Cancelled" ->
            LeaveApplicationStatus.CANCELLED

        else ->
            LeaveApplicationStatus.PENDING
    }

}


private fun parseDate(date: String): Long {

    val formatter = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    )

    return formatter.parse(date)?.time ?: 0L

}

private fun calculateRequestedDays(
    fromDate: Long,
    toDate: Long
): Int {

    return (((toDate - fromDate) / (24 * 60 * 60 * 1000)) + 1).toInt()

}







