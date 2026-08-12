package com.example.workpulse.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CompOffApplicationResponse (
    @SerializedName("data")
    val data : CompOffApplicationData
)


data class CompOffApplicationResponses(
    @SerializedName("data")
    val data: List<CompOffApplicationData>
)
data class CompOffApplicationData(
    @SerializedName("name")
    val name : String,

    @SerializedName("employee")
    val employee : String,

    @SerializedName("leave_type")
    val leaveType : String,

    @SerializedName("work_from_date")
    val fromDate : String,

    @SerializedName("work_to_date")
    val toDate : String,

    @SerializedName("reason")
    val reason : String? = null
)