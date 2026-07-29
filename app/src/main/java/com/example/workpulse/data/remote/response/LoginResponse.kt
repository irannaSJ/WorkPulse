package com.example.workpulse.data.remote.response

import com.google.gson.annotations.SerializedName


data class LoginResponse<T> (
    @SerializedName("message")
    val message : T
)