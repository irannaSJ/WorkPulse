package com.example.workpulse.data.local.dao.config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workpulse.data.local.entity.configEntity.WorkPulseConfigEntity

@Dao
interface WorkPulseConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(config: WorkPulseConfigEntity)

    @Query("SELECT * FROM workpulse_config WHERE id = 1 LIMIT 1")
    suspend fun getConfig(): WorkPulseConfigEntity?

    @Query("DELETE FROM workpulse_config")
    suspend fun deleteAll()
}