package com.example.workpulse.data.local.entity.configEntity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workpulse_quick_actions",
    indices = [
        Index(value = ["configId"]),
        Index(value = ["actionKey"])
    ]
)
data class QuickActionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val configId: Int = 1,
    val actionKey: String,
    val label: String,
    val icon: String?,
    val enabled: Boolean,
    val order: Int,
    val featureKey: String?,
    val permissionRequired: Boolean
)