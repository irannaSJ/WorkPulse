package com.example.workpulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "face_embedding")
data class FaceEmbeddingEntity(

    @PrimaryKey
    val employeeId: String,

    /**
     * L2-normalized MobileFaceNet embedding.
     *
     * Stored as a comma-separated String because Room
     * does not natively persist FloatArray.
     */
    val embedding: String,

    val createdAt: Long,

    val updatedAt: Long,
    /** Template preprocessing version; old unaligned templates must be re-enrolled. */
    val modelVersion: Int
)
