package com.example.workpulse.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class LeaveApplicationRequest(

    @SerializedName("employee")
    val employee: String,

    @SerializedName("leave_type")
    val leaveType: String,

    @SerializedName("from_date")
    val fromDate: String,

    @SerializedName("to_date")
    val toDate: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("leave_approver")
    val leaveApprover: String?

)