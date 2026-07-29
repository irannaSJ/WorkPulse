package com.example.workpulse.data.remote


import com.example.workpulse.data.remote.response.LeaveBalanceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LeaveApi {

    @GET("api/method/frappe.desk.query_report.run")
    suspend fun getEmployeeLeaveBalanceSummary(

        @Query("report_name")
        reportName: String = "Employee Leave Balance Summary",

        @Query("filters")
        filters: String

    ): LeaveBalanceResponse

}