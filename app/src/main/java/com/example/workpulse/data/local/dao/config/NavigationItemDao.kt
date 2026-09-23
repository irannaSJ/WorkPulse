package com.example.workpulse.data.local.dao.config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workpulse.data.local.entity.configEntity.NavigationItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NavigationItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<NavigationItemEntity>)

    @Query("DELETE FROM workpulse_navigation")
    suspend fun deleteAll()

    @Query(
        """
            SELECT * FROM workpulse_navigation
            WHERE configId = 1
            AND enabled = 1
            ORDER BY `order` ASC
        """
    )
    fun observeEnabledItems(): Flow<List<NavigationItemEntity>>

    @Query("""
        SELECT * FROM workpulse_navigation
        WHERE configId = 1
        ORDER BY `order` ASC
    """)
    suspend fun getAll(): List<NavigationItemEntity>

}