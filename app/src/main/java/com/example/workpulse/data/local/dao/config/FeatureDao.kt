package com.example.workpulse.data.local.dao.config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workpulse.data.local.entity.configEntity.FeatureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FeatureEntity>)

    @Query("DELETE FROM workpulse_features")
    suspend fun deleteAll()

    @Query(
        """
        SELECT * FROM workpulse_features
        WHERE configId = 1
        ORDER BY `order` ASC
        """
    )
    fun observeFeatures(): Flow<List<FeatureEntity>>

    @Query("""
        SELECT * FROM workpulse_features
        WHERE configId = 1
        ORDER BY `order` ASC
    """)
    suspend fun getAll() : List<FeatureEntity>
}