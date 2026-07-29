package com.example.workpulse.data.remote.dto


import com.google.gson.annotations.SerializedName

data class EmployeeLeaveBalanceDto(

    @SerializedName("employee")
    val employee: String,

    @SerializedName("employee_name")
    val employeeName: String,

    @SerializedName("department")
    val department: String?,

    @SerializedName("casual_leave")
    val casualLeave: Double,

    @SerializedName("compensatory_off")
    val compensatoryOff: Double,

    @SerializedName("leave_without_pay")
    val leaveWithoutPay: Double,

    @SerializedName("privilege_leave")
    val privilegeLeave: Double,

    @SerializedName("sick_leave")
    val sickLeave: Double

)


data class LeaveBalanceMessage(

    @SerializedName("result")
    val result: List<EmployeeLeaveBalanceDto>

)