package com.example.workpulse.data.remote

import com.example.workpulse.data.remote.dto.request.AttendanceRequestRequest
import com.example.workpulse.data.remote.dto.response.AttendanceRequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AttendanceRequestApi {

    @POST("api/resource/Attendance Request")
    suspend fun createAttendanceRequest(

        @Body request: AttendanceRequestRequest

    ): Response<AttendanceRequestResponse>

}