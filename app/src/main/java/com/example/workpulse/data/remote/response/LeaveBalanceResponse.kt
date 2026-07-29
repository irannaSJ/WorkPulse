package com.example.workpulse.data.remote.response


import com.example.workpulse.data.remote.dto.LeaveBalanceMessage
import com.google.gson.annotations.SerializedName

data class LeaveBalanceResponse(

    @SerializedName("message")
    val message: LeaveBalanceMessage

)