package com.example.workpulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workpulse.feature.attendance.data.local.entity.SyncStatus

@Entity(tableName = "leave_application")
data class LeaveApplicationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Employee Information
    val employeeId: String,

    // Leave Information
    val leaveType: String,

    // Leave Dates
    val fromDate: Long,

    val toDate: Long,

    val requestedDays: Int,

    // Leave Reason
    val reason: String,

    // Application Status
    val applicationStatus: LeaveApplicationStatus,

    // Sync Status
    val syncStatus: SyncStatus,

    // Audit Fields
    val createdAt: Long,

    val updatedAt: Long

)


enum class LeaveApplicationStatus(
    val displayName: String
) {

    PENDING("Pending"),

    APPROVED("Approved"),

    REJECTED("Rejected"),

    CANCELLED("Cancelled")

}