package com.example.workpulse.data.repository

import android.util.Log
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
import com.example.workpulse.data.remote.dto.request.CompOffApplicationRequest
import com.example.workpulse.data.remote.dto.response.CompOffApplicationData
import com.example.workpulse.data.remote.dto.response.ErrorResponse
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus
import com.google.gson.Gson
import com.google.gson.internal.GsonTypes
import okio.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CompOffApplicationRepository @Inject constructor(
    private val compOffApi : CompOffApi,
    private val employeeDao : EmployeeDao,
    private val leaveBalanceDao: LeaveBalanceDao,
    private val sessionManager: SessionManager,
    private val compOffApplicationDao : CompOffApplicationDao,
    private val syncScheduler: SyncScheduler,
    private val gson : Gson
) {

    suspend fun validateCompOffApplication(
        fromDate: Long?,
        toDate: Long?,
        reason: String,
        requestedDays: Int?
    ) : Any {

        Log.d("Error: ","Error in Validate CompOff Application")
        if(fromDate == null){
            return LeaveValidationResult.Error("Please select the From Date")
        }
        if(toDate == null){
            return LeaveValidationResult.Error("Please select the To date")
        }

        if(reason.isBlank()){
            return LeaveValidationResult.Error("Please Enter the Reason")
        }
//        if (requestedDays != null) {
//            if(requestedDays <=0){
//                return LeaveValidationResult.Error("The Requested Days are less than 0")
//            }
//        }
        if(fromDate > toDate){
            return LeaveValidationResult.Error("The From Date cannot be after To Date")
        }

        val duplicateValidation = validateDuplicateCompOff(fromDate=fromDate, toDate = toDate)

        if(duplicateValidation != LeaveValidationResult.Success){
            return duplicateValidation
        }

//        val today = getStartOfToday()
//        if(fromDate < today){
//            return LeaveValidationResult.Error(
//                "From Date Cannot be in the Past"
//            )
//        }
//        if(toDate < today){
//            return LeaveValidationResult.Error(
//                "To Date cannot be in the Past"
//            )
//        }

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

        Log.d(
            "CompOffDuplicate",
            """
    Checking duplicate:
    employeeId=$employeeId
    fromDate=$fromDate
    toDate=$toDate
    existing=$existingCompOff
    """.trimIndent()
        )

        if(existingCompOff != null){

            Log.d(
                "CompOffDuplicate",
                "DUPLICATE FOUND: id=${existingCompOff.id}, " +
                        "erpNextId=${existingCompOff.erpNextId}"
            )
            return LeaveValidationResult.Error(
                "Leave Application Already Exists for the selected Dates"
            )
        }
        Log.d(
            "CompOffSubmit",
            "No duplicate found"
        )

        return LeaveValidationResult.Success
    }

    suspend fun saveCompOffApplication(
        fromDate: Long?,
        toDate: Long?,
        reason: String,
        requestedDays: Int?
    ): LeaveApplicationResult{


        return try {

            val employee = employeeDao.getEmployeeOnce()
                ?:return LeaveApplicationResult.Error("Employee Not Found")

            Log.d(
                "CompOffSubmit",
                "Starting saveCompOffApplication"
            )

            when(
                val validationResult = validateCompOffApplication(
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
                    Log.d(
                        "CompOffSubmit",
                        "Validation successful. Inserting into Room: $compOffApplication"
                    )
                    compOffApplicationDao.insertComposeOffApplication(
                        compOffApplication
                    )
                    Log.d(
                        "CompOffSubmit",
                        "Room INSERT completed. local entity = $compOffApplication"
                    )
                    syncScheduler.scheduleCompOffSync()
                    LeaveApplicationResult.Success

                }
                else -> {
                    Log.d("Comp Off application :", "Comp off application failed to save")
                    LeaveApplicationResult.ValidationFailed(validationResult as LeaveValidationResult)
                }
            }
        } catch (exception: Exception) {

            LeaveApplicationResult.Error(
                exception.message ?: "Failed to save leave application."
            )

        }

    }



    private fun extractServerError(
        errorBody: String?
    ): String {

        if (errorBody.isNullOrBlank()) {
            return "Failed to submit Comp Off request"
        }

        return try {

            val json = gson.fromJson(
                errorBody,
                ErrorResponse::class.java
            )

            json.exception
                ?.substringAfter("ValidationError: ")
                ?.trim()
                ?: "Failed to submit Comp Off request"

        } catch (e: Exception) {

            "Failed to submit Comp Off request"
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

    suspend fun syncPendingCompOffApplications(){
        updatePendingCompOffApplication()
        downloadLatestCompOffApplications()
    }


    private suspend fun updatePendingCompOffApplication(){
        val pendingApplications = compOffApplicationDao.getPendingCompOffApplication()
        for (application in pendingApplications){
            val request = CompOffApplicationRequest(
                employee = application.employeeId,
                leaveType = application.leaveType,
                fromDate = formatDate(application.fromDate),
                toDate = formatDate(application.toDate),
                reason = application.reason
            )

            try {
                val response = compOffApi.createCompOffApplication(request)
                if(response.isSuccessful){
                    val erpNextId = response.body()?.data?.name?:continue

                    compOffApplicationDao.updateSyncDetails(
                        id = application.id,
                        erpNextId = erpNextId,
                        syncStatus = SyncStatus.SYNCED,
                    )
                } else{
                    val errorMessage = extractServerError(response.errorBody()?.string())
                    Log.e(
                        "CompOffSync",
                        "ERPNext rejected Comp Off request: $errorMessage"
                    )
                    compOffApplicationDao.updateSyncFailure(
                        id = application.id,
                        syncStatus = SyncStatus.FAILED,
                        compOffApplicationStatus = ApplicationStatus.REJECTED,
                        errorMessage = errorMessage
                    )
                }
            }catch (e : IOException){
                throw e
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    private suspend fun downloadLatestCompOffApplications(){
        Log.d("download Compoff application:", "Comp Off application Download file is reached")
        val employeeId = sessionManager.getEmployeeId()
        val filters = """
            [ ["employee", "=", "$employeeId"]]
        """.trimIndent()
        val fields="""
            [
                "name",
                "employee_name",
                "employee",
                "work_from_date",
                "work_end_date",
                "docstatus",
                "reason"
            ]
        """.trimIndent()

        val response = compOffApi.getCompOffApplication(
            fields = fields,
            filters= filters,
            limit = 15

        )
        Log.d("CompOff Application ", " Downloaded ${response.data.size} compoff applications")

//        for(dto in response.data) {
        response.data.forEach { dto ->
            val entity = dto.toEntity()
            val existing = compOffApplicationDao.getByErpNextId(dto.name)
            if (existing == null){
                compOffApplicationDao.insertComposeOffApplication(entity)

            }else{
                compOffApplicationDao.updateComposeOffApplication(
                    entity.copy(id = existing.id)
                )
            }
        }
    }

    private fun CompOffApplicationData.toEntity() : CompOffApplicationEntity{
        return CompOffApplicationEntity(
            id = 0,
            employeeId = employee,
            erpNextId = name,
            leaveType = "Compensatory Off",
            fromDate = parseDate(fromDate),
            toDate = parseDate(toDate),
            reason = reason.orEmpty(),
            compOffApplicationStatus = mapStatus(status),
            syncStatus = SyncStatus.SYNCED,
        )
    }

}

private fun mapStatus(
    status: Int
): ApplicationStatus{
    return when(status){
        0 -> ApplicationStatus.PENDING
        1 -> ApplicationStatus.SUBMITTED
        2 -> ApplicationStatus.CANCELLED
        else -> ApplicationStatus.PENDING
    }
}




private fun formatDate(
    millis : Long
) : String{
    return java.text.SimpleDateFormat(
        "yyyy-MM-dd",
        java.util.Locale.getDefault()
    ).format(java.util.Date(millis))
}

private fun parseDate(date : String) : Long{
    val formatter = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    )
    return formatter.parse(date)?.time ?: 0L
}