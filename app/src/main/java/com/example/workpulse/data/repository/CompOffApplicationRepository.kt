package com.example.workpulse.data.repository

import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.core.worker.SyncScheduler
import com.example.workpulse.data.local.dao.CompOffApplicationDao
import com.example.workpulse.data.local.dao.EmployeeDao
import com.example.workpulse.data.local.dao.LeaveBalanceDao
import com.example.workpulse.data.remote.CompOffApi
import com.example.workpulse.feature.leaveApplication.LeaveApplicationResult
import com.example.workpulse.feature.leaveApplication.LeaveValidationResult
import java.util.Calendar
import com.example.workpulse.data.local.entity.ApplicationStatus
import com.example.workpulse.data.local.entity.CompOffApplicationEntity
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import javax.inject.Inject

class CompOffApplicationRepository @Inject constructor(
    private val compOffApi : CompOffApi,
    private val employeeDao : EmployeeDao,
    private val leaveBalanceDao: LeaveBalanceDao,
    private val sessionManager: SessionManager,
    private val compOffApplicationDao : CompOffApplicationDao,
    private val syncScheduler: SyncScheduler
) {

    suspend fun validateCompOffApplication(
        leaveType: String,
        fromDate: Long?,
        toDate: Long?,
        reason: String,
        requestedDays: Int?
    ) : Any {

        if(leaveType == null){
            return LeaveValidationResult.Error("Please mention the Leave Type")
        }

        if(fromDate == null){
            return LeaveValidationResult.Error("Please select the From Date")
        }
        if(toDate == null){
            return LeaveValidationResult.Error("Please select the To date")
        }

        if(reason.isBlank()){
            return LeaveValidationResult.Error("Please Enter the Reason")
        }
        if (requestedDays != null) {
            if(requestedDays <=0){
                return LeaveValidationResult.Error("The Requested Days are less then 0")
            }
        }
        if(fromDate > toDate){
            return LeaveValidationResult.Error("The From Date cannot be after To Date")
        }

        val duplicateValidation = validateDuplicateCompOff(fromDate=fromDate, toDate = toDate)

        if(duplicateValidation != LeaveValidationResult.Success){
            return duplicateValidation
        }

        val today = getStartOfToday()
        if(fromDate < today){
            return LeaveValidationResult.Error(
                "From Date Cannot be in the Past"
            )
        }
        if(toDate < today){
            return LeaveValidationResult.Error(
                "To Date cannot be in the Past"
            )
        }



        return LeaveValidationResult.Success
    }

    private suspend fun validateDuplicateCompOff(
        fromDate: Long?,
        toDate: Long?
    ): LeaveValidationResult{

        val employeeId = sessionManager.getEmployeeId()

        val existingCompOff = compOffApplicationDao.getOverlappingLeaveApplication(
            employeeId = employeeId,
            fromDate = fromDate,
            toDate = toDate
        )

        if(existingCompOff != null){
            return LeaveValidationResult.Error(
                "Leave Application Already Exists for the selected Dates"
            )
        }

        return LeaveValidationResult.Success
    }

    suspend fun saveCompOffApplication(
        leaveType: String,
        fromDate: Long?,
        toDate: Long?,
        reason: String,
        requestedDays: Int?
    ): LeaveApplicationResult{


        return try {

            val employee = employeeDao.getEmployeeOnce()
                ?:return LeaveApplicationResult.Error("Employee Not Found")
            when(
                val validationResult = validateCompOffApplication(
                    leaveType = leaveType,
                    fromDate = fromDate,
                    toDate = toDate,
                    reason = reason,
                    requestedDays = requestedDays
                )
            ){
                is LeaveValidationResult.Success -> {
                    val currentTime = System.currentTimeMillis()
                    val compOffApplication = CompOffApplicationEntity(
                        employeeId = employee.employeeId,
                        leaveType = "Compensatory Off",
                        fromDate = fromDate!!,
                        toDate = toDate!!,
                        reason = reason.trim(),
                        compOffApplicationStatus = ApplicationStatus.PENDING,
                        syncStatus = SyncStatus.PENDING
                    )
                    compOffApplicationDao.insertComposeOffApplication(
                        compOffApplication
                    )
                    LeaveApplicationResult.Success

                }
                else -> {
                    LeaveApplicationResult.ValidationFailed(validationResult as LeaveValidationResult)
                }
            }
        } catch (exception: Exception) {

            LeaveApplicationResult.Error(
                exception.message ?: "Failed to save leave application."
            )

        }

    }



    private fun getStartOfToday() : Long{
        val calander  = Calendar.getInstance()
        calander.set(
            Calendar.HOUR_OF_DAY,
            0
        )

        calander.set(
            Calendar.MINUTE,
            0
        )

        calander.set(
            Calendar.SECOND,
            0
        )

        calander.set(
            Calendar.MILLISECOND,
            0
        )

        return calander.timeInMillis
    }
}