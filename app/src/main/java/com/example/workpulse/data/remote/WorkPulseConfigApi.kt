package com.example.workpulse.data.remote

import com.example.workpulse.data.remote.dto.response.WorkPulseConfigResponseDto
import retrofit2.http.GET

interface WorkPulseConfigApi {
    @GET("api/method/hrms_customization.api.workpulse.get_workpulse_configuration")
    suspend fun getWorkPulseConfiguration(): WorkPulseConfigResponseDto
}