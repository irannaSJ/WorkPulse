package com.example.workpulse.feature.leave

import android.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class LeaveTypeUiModel(

    val name: String,

    val used: Int,

    val total: Int,

    val remaining: Int,

    val progress: Float,

    val icon: ImageVector,

    val color: Color

)