package com.example.workpulse.data.remote

import com.example.workpulse.data.remote.dto.request.CompOffApplicationRequest
import com.example.workpulse.data.remote.dto.response.CompOffApplicationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface CompOffApi {
    @POST("api/resource/Compensatory Leave Request")
    suspend fun createCompOffApplication(
        @Body
        request : CompOffApplicationRequest
    ): Response<CompOffApplicationResponse>


    @GET("api/resource/Compensatory Leave Request")
    suspend fun getCompOffApplication(
        @Query("fields")
        fields : String,

        @Query("filters")
        filters : String,

        @Query("order_by")
        orderBy : String = "from_date desc"
    ) : CompOffApplicationResponse
}