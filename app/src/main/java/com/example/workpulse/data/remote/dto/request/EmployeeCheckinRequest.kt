package com.example.workpulse.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class EmployeeCheckinRequest(

    @SerializedName("employee")
    val employee: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("log_type")
    val logType: String,

    @SerializedName("device_id")
    val deviceId: String,

    @SerializedName("latitude")
    val latitude: Double? = null,

    @SerializedName("longitude")
    val longitude: Double? = null,

    @SerializedName("skip_auto_attendance")
    val skipAutoAttendance: Int = 0

)