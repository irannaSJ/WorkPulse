package com.example.workpulse.data.repository

import com.example.workpulse.data.local.dao.FaceEmbeddingDao
import com.example.workpulse.data.local.entity.FaceEmbeddingEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceEmbeddingRepository @Inject constructor(
    private val faceEmbeddingDao: FaceEmbeddingDao
) {

    suspend fun saveEmbedding(
        employeeId: String,
        embedding: FloatArray
    ) = withContext(Dispatchers.IO) {

        require(embedding.size == 192) {
            "Face embedding must contain 192 values"
        }

        val embeddingString = embedding.joinToString(",")

        val now = System.currentTimeMillis()

        faceEmbeddingDao.saveFaceEmbedding(
            FaceEmbeddingEntity(
                employeeId = employeeId,
                embedding = embeddingString,
                createdAt = now,
                updatedAt = now,
                modelVersion = CURRENT_MODEL_VERSION
            )
        )
    }

    suspend fun getEmbedding(
        employeeId: String
    ): FloatArray? = withContext(Dispatchers.IO) {

        val entity = faceEmbeddingDao.getFaceEmbedding(employeeId)
            ?: return@withContext null

        if (entity.modelVersion != CURRENT_MODEL_VERSION) return@withContext null

        entity.embedding
            .split(",")
            .map { it.toFloat() }
            .toFloatArray()
    }

    suspend fun hasEmbedding(
        employeeId: String
    ): Boolean = withContext(Dispatchers.IO) {

        faceEmbeddingDao.hasFaceEmbedding(employeeId)
    }

    suspend fun deleteEmbedding(
        employeeId: String
    ) = withContext(Dispatchers.IO) {

        faceEmbeddingDao.deleteFaceEmbedding(employeeId)
    }

    private companion object { const val CURRENT_MODEL_VERSION = 2 }
}
