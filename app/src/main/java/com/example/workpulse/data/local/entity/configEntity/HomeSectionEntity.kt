package com.example.workpulse.data.local.entity.configEntity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workpulse_home_sections",
    indices = [
        Index(value = ["configId"]),
        Index(value = ["sectionKey"])
    ]
)
data class HomeSectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val configId: Int = 1,
    val sectionKey: String,
    val title: String,
    val sectionType: String,
    val enabled: Boolean,
    val order: Int,
    val featureKey: String?,
    val permissionRequired: Boolean,
    val configuration: String?
)