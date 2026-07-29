package com.example.workpulse.feature.leaveApplication

import com.squareup.wire.Message

sealed interface LeaveValidationResult {

    data object Success : LeaveValidationResult

    data class Error(
        val message: String
    ) : LeaveValidationResult

    data class InsufficientBalance(
        val availableBalance: Double,
        val suggestions: List<LeaveSuggestionUi>
    ) : LeaveValidationResult

}


sealed interface LeaveApplicationResult {

    data object Success : LeaveApplicationResult

    data class ValidationFailed(
        val validationResult: LeaveValidationResult
    ) : LeaveApplicationResult

    data class Error(
        val message: String
    ) : LeaveApplicationResult

}