package com.example.workpulse.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class AttendanceRequestListResponse (
    @SerializedName("data")
    val data: List<AttendanceRequestServerData> = emptyList()
)

data class AttendanceRequestServerData(

    @SerializedName("name")
    val name: String,

    @SerializedName("employee")
    val employee: String,

    @SerializedName("from_date")
    val fromDate: String?,

    @SerializedName("to_date")
    val toDate: String?,

    @SerializedName("request_type")
    val requestType: String?,

    @SerializedName("include_holidays")
    val includeHolidays: Int = 0,

    @SerializedName("reason")
    val reason: String?,

    @SerializedName("docstatus")
    val docStatus: Int,

    @SerializedName("explanation")
    val explanation : String?
)