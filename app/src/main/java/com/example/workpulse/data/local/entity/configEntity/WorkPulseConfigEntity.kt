package com.example.workpulse.data.local.entity.configEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workpulse_config")
data class WorkPulseConfigEntity (
    @PrimaryKey
    val id : Int =1,
    val configurationName : String,
    val version: Int,
    val syncedAt: Long = System.currentTimeMillis()
)