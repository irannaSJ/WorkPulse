package com.example.workpulse.data.remote.response

import com.google.gson.annotations.SerializedName

data class EmployeeResponse(
    val data: List<EmployeeData>
)

data class EmployeeData(
    @SerializedName("name")
    val name: String,

    @SerializedName("employee_name")
    val employeeName: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("company")
    val company: String?,

    @SerializedName("department")
    val department: String?,

    @SerializedName("designation")
    val designation: String?,

    @SerializedName("company_email")
    val companyEmail: String?,

    @SerializedName("personal_email")
    val personalEmail: String?,

    @SerializedName("cell_number")
    val mobileNumber: String?,

    @SerializedName("date_of_joining")
    val dateOfJoining : String?,

    @SerializedName("current_address")
    val currentAddress : String?,

    @SerializedName("image")
    val profileImage: String?,

    @SerializedName("leave_approver")
    val leaveApprover : String?,
)