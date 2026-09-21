package com.example.workpulse.data.local.dao.config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workpulse.data.local.entity.configEntity.HomeSectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeSectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HomeSectionEntity>)

    @Query("DELETE FROM workpulse_home_sections")
    suspend fun deleteAll()

    @Query(
        """
        SELECT * FROM workpulse_home_sections
        WHERE configId = 1
        AND enabled = 1
        ORDER BY `order` ASC
        """
    )
    fun observeEnabledSections(): Flow<List<HomeSectionEntity>>

    @Query("""
        SELECT * FROM workpulse_home_sections
        WHERE configId =1
        ORDER BY `order` ASC
    """)
    suspend fun getAll(): List<HomeSectionEntity>
}