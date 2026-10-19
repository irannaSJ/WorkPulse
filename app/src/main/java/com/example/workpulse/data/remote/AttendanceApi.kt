package com.example.workpulse.data.remote
import com.example.workpulse.data.remote.dto.request.EmployeeCheckinRequest
import com.example.workpulse.data.remote.dto.response.EmployeeCheckinResponses
import com.example.workpulse.data.remote.dto.response.PunchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AttendanceApi {

//    @POST("/api/resource/Employee Checkin")
//    suspend fun createEmployeeCheckin(
//        @Body request: EmployeeCheckinRequest
//    ): Response<Any>

    @FormUrlEncoded
    @POST("/api/method/hrms_customization.api.attendance.punch_in")
    suspend fun punchIn(
        @Field("punch_time") punchTime : String,
        @Field("device_id") deviceId : String?,
        @Field("latitude") latitude : Double?,
        @Field("longitude") longitude: Double?
    ): Response<PunchResponse>

    @FormUrlEncoded
    @POST("/api/method/hrms_customization.api.attendance.punch_out")
    suspend fun punchOut(
        @Field("punch_time") punchTime : String,
        @Field("device_id") deviceId : String?,
        @Field("latitude") latitude : Double?,
        @Field("longitude") longitude: Double?
    ): Response<PunchResponse>




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



