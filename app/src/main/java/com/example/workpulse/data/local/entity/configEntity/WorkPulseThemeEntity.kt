package com.example.workpulse.data.local.entity.configEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workpulse_theme")
data class WorkPulseThemeEntity(
    @PrimaryKey
    val configId: Int = 1,

    val themeMode: String,
    val primaryColor: String?,
    val secondaryColor: String?,
    val accentColor: String?,
    val backgroundColor: String?,
    val surfaceColor: String?,
    val primaryTextColor: String?,
    val secondaryTextColor: String?,
    val darkBackgroundColor: String?,
    val darkSurfaceColor: String?,
    val darkPrimaryTextColor: String?,
    val darkSecondaryTextColor: String?,
    val cornerRadius: Double
)