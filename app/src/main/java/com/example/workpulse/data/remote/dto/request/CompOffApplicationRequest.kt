package com.example.workpulse.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CompOffApplicationRequest (
    @SerializedName("employee")
    val employee : String,

    @SerializedName("leave_type")
    val leaveType : String,

    @SerializedName("work_from_date")
    val fromDate  : String,

    @SerializedName("work_to_date")
    val toDate : String,

    @SerializedName("reason")
    val reason : String? = null


)