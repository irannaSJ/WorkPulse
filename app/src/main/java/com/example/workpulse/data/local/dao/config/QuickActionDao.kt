package com.example.workpulse.data.local.dao.config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workpulse.data.local.entity.configEntity.QuickActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickActionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items : List<QuickActionEntity>)

    @Query("DELETE FROM workpulse_quick_actions")
    suspend fun deleteAll()

    @Query(
        """
        SELECT * FROM workpulse_quick_actions
        WHERE configId = 1
        AND enabled = 1
        ORDER BY `order` ASC
        """
    )
    fun observeEnabledItems(): Flow<List<QuickActionEntity>>

    @Query("""
        SELECT * FROM workpulse_quick_actions
        WHERE configId = 1
        ORDER BY `order` ASC
    """)
    suspend fun getAll(): List<QuickActionEntity>
}