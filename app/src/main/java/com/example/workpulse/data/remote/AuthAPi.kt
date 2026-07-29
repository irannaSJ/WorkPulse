package com.example.workpulse.data.remote

import com.example.workpulse.core.network.NetworkConstants
import com.example.workpulse.data.remote.response.EmployeeResponse
import com.example.workpulse.data.remote.response.LoggedUserResponse
import com.example.workpulse.data.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

import retrofit2.http.Query

interface AuthApi {

    @FormUrlEncoded
    @POST(NetworkConstants.LOGIN)
    suspend fun login(
        @Field("usr")
        username: String,

        @Field("pwd")
        password: String
    ): Response<LoginResponse<String>>

    @GET("api/method/frappe.auth.get_logged_user")
    suspend fun getLoggedUser(): Response<LoggedUserResponse>
    @POST(NetworkConstants.LOGOUT)
    suspend fun logout(): Response<Unit>


    @GET("api/resource/Employee")
    suspend fun getEmployee(
        @Query("filters") filters: String,
        @Query("fields") fields: String
    ): Response<EmployeeResponse>
}