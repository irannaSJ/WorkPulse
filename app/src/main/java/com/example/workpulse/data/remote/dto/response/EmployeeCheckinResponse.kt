package com.example.workpulse.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class PunchResponse(
    val message : PunchMessage
)

data class PunchMessage(
    val success : Boolean,
    val checkin : EmployeeCheckinData
)


data class EmployeeCheckinResponses(
    @SerializedName("data")
    val data : List<EmployeeCheckinData>
)
data class EmployeeCheckinData(
    @SerializedName("name")
    val name : String,

    @SerializedName("employee")
    val employee : String,

    @SerializedName("time")
    val time : String,

    @SerializedName("log_type")
    val logType : String,

    @SerializedName("device_id")
    val deviceId : String
)