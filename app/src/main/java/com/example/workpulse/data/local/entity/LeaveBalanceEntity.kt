package com.example.workpulse.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workpulse.data.remote.dto.EmployeeLeaveBalanceDto

@Entity(tableName = "leave_balance")
data class LeaveBalanceEntity(

    @PrimaryKey
    val employeeId: String,

    val employeeName: String,

    val department: String?,

    val casualLeave: Double,

    val compensatoryOff: Double,

    val leaveWithoutPay: Double,

    val privilegeLeave: Double,

    val sickLeave: Double,

    val lastSynced: Long

)


fun EmployeeLeaveBalanceDto.toEntity(
    lastSynced: Long = System.currentTimeMillis()
): LeaveBalanceEntity {

    return LeaveBalanceEntity(
        employeeId = employee,
        employeeName = employeeName,
        department = department,
        casualLeave = casualLeave,
        compensatoryOff = compensatoryOff,
        leaveWithoutPay = leaveWithoutPay,
        privilegeLeave = privilegeLeave,
        sickLeave = sickLeave,
        lastSynced = lastSynced
    )
}