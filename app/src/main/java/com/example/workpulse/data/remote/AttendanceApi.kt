package com.example.workpulse.data.remote
import com.example.workpulse.data.remote.dto.request.EmployeeCheckinRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AttendanceApi {

    @POST("/api/resource/Employee Checkin")
    suspend fun createEmployeeCheckin(
        @Body request: EmployeeCheckinRequest
    ): Response<Any>

}