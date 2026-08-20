package com.example.workpulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee")
data class EmployeeEntity(

    @PrimaryKey
    val employeeId: String,

    val userId: String,

    val employeeName: String,

    val company: String? = null,

    val department: String? = null,

    val designation: String? = null,

    val companyEmail: String? = null,

    val personalEmail: String? = null,

    val mobileNumber: String? = null,
    val dateOfJoining : String? =null,
    val currentAddress : String? = null,

    val profileImage: String? = null,

    val updatedAt: Long,
    val leaveApprover : String? = null
)