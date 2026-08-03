package com.example.workpulse.data.remote.dto.response


import com.google.gson.annotations.SerializedName

data class LeaveApplicationResponse(

    @SerializedName("data")
    val data: LeaveApplicationData

)

data class LeaveApplicationData(

    @SerializedName("name")
    val name: String,

    @SerializedName("employee")
    val employee: String,

    @SerializedName("leave_type")
    val leaveType: String,

    @SerializedName("status")
    val status: String

)