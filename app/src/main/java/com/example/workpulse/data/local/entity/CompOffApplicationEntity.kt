package com.example.workpulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus

@Entity(tableName = "compOff_application")
data class CompOffApplicationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val erpNextId : String? = null,
    val employeeId : String,
    val leaveType : String = "Compensatory Off",
    val reason : String,
    val fromDate : Long,
    val toDate : Long,
    val syncStatus : SyncStatus,
    val compOffApplicationStatus : ApplicationStatus


)

enum class ApplicationStatus(
    val displayName : String
){
    PENDING("Pending"),
    SUBMITTED("Submitted"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected")
}