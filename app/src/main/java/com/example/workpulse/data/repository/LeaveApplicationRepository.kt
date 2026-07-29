package com.example.workpulse.data.repository

import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.data.local.dao.EmployeeDao
import com.example.workpulse.data.local.dao.LeaveApplicationDao
import com.example.workpulse.data.local.dao.LeaveBalanceDao
import com.example.workpulse.data.local.entity.LeaveApplicationEntity
import com.example.workpulse.data.local.entity.LeaveApplicationStatus
import com.example.workpulse.data.remote.LeaveApi
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import com.example.workpulse.feature.leaveApplication.LeaveApplicationResult
import com.example.workpulse.feature.leaveApplication.LeaveSuggestionUi
import com.example.workpulse.feature.leaveApplication.LeaveType
import com.example.workpulse.feature.leaveApplication.LeaveValidationResult
import javax.inject.Inject

class LeaveApplicationRepository @Inject constructor(
    private val leaveApi : LeaveApi,
    private val employeeDao: EmployeeDao,
    private val leaveBalanceDao: LeaveBalanceDao,
    private val sessionManager: SessionManager,
    private val leaveApplicationDao: LeaveApplicationDao
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
        if(requestedDays <=0){
            return LeaveValidationResult.Error(
                "The Requested Days are less then 0"
            )
        }

        val availableBalance = getAvailableBalance(leaveType)

        if(availableBalance <= requestedDays){
            val suggestions = getLeaveSuggestions(
                selectedLeaveType =leaveType,
                requestedDays = requestedDays
            )

            return LeaveValidationResult.InsufficientBalance(availableBalance = availableBalance, suggestions = suggestions)
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

                        leaveType = leaveType!!.name,

                        fromDate = fromDate!!,

                        toDate = toDate!!,

                        requestedDays = requestedDays,

                        reason = reason.trim(),

                        applicationStatus = LeaveApplicationStatus.PENDING,

                        syncStatus = SyncStatus.PENDING,

                        createdAt = currentTime,

                        updatedAt = currentTime

                    )

                    leaveApplicationDao.insertLeaveApplication(
                        leaveApplication
                    )

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
}