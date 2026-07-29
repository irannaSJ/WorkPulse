package com.example.workpulse.data.repository

import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.data.local.dao.LeaveBalanceDao
import com.example.workpulse.data.local.entity.LeaveBalanceEntity
import com.example.workpulse.data.local.entity.toEntity
import com.example.workpulse.data.remote.LeaveApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaveRepository @Inject constructor(

    private val leaveApi: LeaveApi,
    private val leaveBalanceDao: LeaveBalanceDao,
    private val sessionManager: SessionManager

) {

    /**
     * Observe leave balance from Room.
     * UI should always observe Room (Single Source of Truth).
     */
    fun observeLeaveBalance(): Flow<LeaveBalanceEntity?> {

        return sessionManager.employeeIdFlow
            .flatMapLatest { employeeId ->
                leaveBalanceDao.observeLeaveBalance(employeeId)
            }
    }

    /**
     * Download latest leave balance from ERPNext
     * and save it into Room.
     */
    suspend fun syncLeaveBalance() {

        try {

            val employeeId = sessionManager.getEmployeeId()

            if (employeeId.isBlank()) return

            val filters = createFilters(
                company = "Datamann",
                date = LocalDate.now().toString()
            )

            val response = leaveApi.getEmployeeLeaveBalanceSummary(
                filters = filters
            )

            response.message.result
                .firstOrNull { it.employee == employeeId }
                ?.let { dto ->

                    leaveBalanceDao.insertLeaveBalance(
                        dto.toEntity()
                    )

                }

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    /**
     * Creates ERPNext report filters.
     */
    private fun createFilters(
        company: String,
        date: String
    ): String {

        return """
            {
                "date":"$date",
                "company":"$company",
                "employee_status":"Active"
            }
        """.trimIndent()
    }
}