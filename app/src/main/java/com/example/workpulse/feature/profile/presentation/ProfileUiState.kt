package com.example.workpulse.feature.profile.presentation

data class ProfileUiState(

    // Loading State
    val isLoading: Boolean = false,

    // Employee Details
    val employeeId: String = "",
    val employeeName: String = "",
    val employeeImage: String = "",

    // Organization Details (Read Only)
    val company: String = "",
    val department: String = "",
    val designation: String = "",
    val reportsTo: String = "",
    val dateOfJoining: String = "",

    // Contact Information
    val companyEmail: String = "",
    val personalEmail: String = "",
    val mobileNumber: String = "",
    val emergencyContact: String = "",
    val currentAddress: String = "",

    // Personal Information
    val gender: String = "",
    val dateOfBirth: String = "",

    // UI State
    val isUpdating: Boolean = false,
    val isUpdateSuccessful: Boolean = false,
    val errorMessage: String? = null,
    val isLogoutSuccessful: Boolean = false,

    val isEditing : Boolean = false,

    val imageUri: String = "",
    val app_uid : String = ""

)