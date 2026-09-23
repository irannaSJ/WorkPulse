package com.example.workpulse.data.remote
import com.example.workpulse.data.remote.dto.request.EmployeeCheckinRequest
import com.example.workpulse.data.remote.dto.response.EmployeeCheckinResponse
import com.example.workpulse.data.remote.dto.response.EmployeeCheckinResponses
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AttendanceApi {

    @POST("/api/resource/Employee Checkin")
    suspend fun createEmployeeCheckin(
        @Body request: EmployeeCheckinRequest
    ): Response<Any>



    @GET("api/resource/Employee Checkin")
    suspend fun getEmployeeCheckins(
        @Query("fields")
        fields : String,

        @Query("filters")
        filters : String,

        @Query("order_by")
        orderBy : String = "time asc",

        @Query("limit_page_length")
        limitPageLength : Int = 20
    ) : Response<EmployeeCheckinResponses>

}



