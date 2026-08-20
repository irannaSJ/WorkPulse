package com.example.workpulse.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class ErrorResponse (
    @SerializedName("exception")
    val exception : String? = null,

    @SerializedName("_server_messages")
    val serverMessages : String? = null
)