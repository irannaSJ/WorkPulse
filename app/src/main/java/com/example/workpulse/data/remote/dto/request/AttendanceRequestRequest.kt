package com.example.workpulse.data.remote.dto.request


import com.google.gson.annotations.SerializedName
data class AttendanceRequestRequest (
    @SerializedName("employee")
    val employee : String,

    @SerializedName("from_date")
    val fromDate : String,

    @SerializedName("to_date")
    val toDate : String,

    @SerializedName("reason")
    val requestType : String,

    @SerializedName("include_holidays")
    val includeHolidays : Boolean,

    @SerializedName("explanation")
    val reason : String

)