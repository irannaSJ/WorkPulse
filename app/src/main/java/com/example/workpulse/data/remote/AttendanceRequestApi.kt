package com.example.workpulse.data.remote

import com.example.workpulse.data.remote.dto.request.AttendanceRequestListResponse
import com.example.workpulse.data.remote.dto.request.AttendanceRequestRequest
import com.example.workpulse.data.remote.dto.response.AttendanceRequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AttendanceRequestApi {

    @POST("api/resource/Attendance Request")
    suspend fun createAttendanceRequest(

        @Body request: AttendanceRequestRequest

    ): Response<AttendanceRequestResponse>


    @GET("api/resource/Attendance Request")
    suspend fun getAttendanceRequests(
        @Query("filters")
        filters : String,

        @Query("fields")
        fields : String,

        @Query("order_by")
        orderBy : String = "creation desc",

        @Query("limit_page_length")
        limit : Int = 15
    ) : Response<AttendanceRequestListResponse>

}