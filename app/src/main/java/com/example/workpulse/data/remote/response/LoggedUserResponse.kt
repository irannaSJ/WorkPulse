package com.example.workpulse.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoggedUserResponse(

    @SerializedName("message")
    val message: String
)