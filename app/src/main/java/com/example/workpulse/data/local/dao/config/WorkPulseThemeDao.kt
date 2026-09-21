package com.example.workpulse.data.local.dao.config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workpulse.data.local.entity.configEntity.WorkPulseThemeEntity

@Dao
interface WorkPulseThemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(theme: WorkPulseThemeEntity)

    @Query("SELECT * FROM workpulse_theme WHERE configId = 1 LIMIT 1")
    suspend fun getTheme(): WorkPulseThemeEntity?

    @Query("DELETE FROM workpulse_theme")
    suspend fun deleteAll()

}