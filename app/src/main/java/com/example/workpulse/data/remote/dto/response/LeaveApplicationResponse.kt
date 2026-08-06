package com.example.workpulse.data.remote.dto.response


import com.google.gson.annotations.SerializedName

data class LeaveApplicationResponse(

    @SerializedName("data")
    val data: LeaveApplicationData

)


data class LeaveApplicationResponses(
    @SerializedName("data")
    val data : List<LeaveApplicationData>
)

data class LeaveApplicationData(

    @SerializedName("name")
    val name: String,

    @SerializedName("employee")
    val employee: String,

    @SerializedName("leave_type")
    val leaveType: String,

    @SerializedName("from_date")
    val fromDate : String,

    @SerializedName("to_date")
    val toDate : String,

    @SerializedName("status")
    val status: String,

    @SerializedName("description")
    val description : String? = null

)