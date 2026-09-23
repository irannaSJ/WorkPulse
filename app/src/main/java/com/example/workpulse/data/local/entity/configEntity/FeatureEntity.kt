package com.example.workpulse.data.local.entity.configEntity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workpulse_features",
    indices = [
        Index(value = ["configId"]),
        Index(value = ["featureKey"])
    ]
)
data class FeatureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val configId: Int = 1,
    val featureKey: String,
    val displayName: String,
    val enabled: Boolean,
    val order: Int,
    val configuration: String?,
    val permissionRequired: Boolean
)