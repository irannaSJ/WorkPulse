package com.example.workpulse.data.local.entity.configEntity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workpulse_navigation",
    indices = [
        Index(value = ["configId"]),
        Index(value = ["navigationKey"])
    ]
)
data class NavigationItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val configId: Int = 1,
    val navigationKey: String,
    val label: String,
    val icon: String?,
    val location: String,
    val enabled: Boolean,
    val order: Int,
    val featureKey: String?,
    val permissionRequired: Boolean
)