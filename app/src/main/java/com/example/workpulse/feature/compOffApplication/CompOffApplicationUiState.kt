package com.example.workpulse.feature.compOffApplication

data class CompOffApplicationUiState(
    val employeeId : String = "",
    val employeeName : String = "",
    val company : String = "",
//    val leaveType : String = "Compensatory Off",
    val fromDate : Long? = null,
    val toDate : Long? = null,
    val requestedDays : Int? = null,
    val reason : String = "",
    val isLoading : Boolean = false,
    val isSubmitting : Boolean = false,
    val errorMessage : String? = null,
    val successMessage : String? = null,
)