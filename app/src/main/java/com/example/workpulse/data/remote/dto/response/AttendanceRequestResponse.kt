package com.example.workpulse.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class AttendanceRequestResponse(
    @SerializedName("data")
    val data: AttendanceRequestData
)

data class AttendanceRequestData(
    @SerializedName("name")
    val name : String
)