package com.example.workpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workpulse.data.local.entity.FaceEmbeddingEntity

@Dao
interface FaceEmbeddingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFaceEmbedding(
        faceEmbedding: FaceEmbeddingEntity
    )

    @Query(
        "SELECT * FROM face_embedding WHERE employeeId = :employeeId LIMIT 1"
    )
    suspend fun getFaceEmbedding(
        employeeId: String
    ): FaceEmbeddingEntity?

    @Query(
        "SELECT EXISTS(" +
                "SELECT 1 FROM face_embedding " +
                "WHERE employeeId = :employeeId AND modelVersion = 2" +
                ")"
    )
    suspend fun hasFaceEmbedding(
        employeeId: String
    ): Boolean

    @Query(
        "DELETE FROM face_embedding WHERE employeeId = :employeeId"
    )
    suspend fun deleteFaceEmbedding(
        employeeId: String
    )
}
