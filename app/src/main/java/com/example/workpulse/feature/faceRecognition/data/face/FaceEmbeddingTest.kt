package com.example.workpulse.feature.faceRecognition.data.face

import android.graphics.Bitmap
import android.util.Log

class FaceEmbeddingTest {

    fun runTest(
        model: FaceEmbeddingModel,
        bitmap: Bitmap
    ) {

        val converter = FaceTensorConverter()

        val image = converter.bitmapToFloatArray(bitmap)

        val embeddings = model.generateEmbeddings(
            firstImage = image,
            secondImage = image
        )

        Log.d(
            "FaceEmbeddingTest",
            "Embedding 0 size = ${embeddings[0].size}"
        )

        Log.d(
            "FaceEmbeddingTest",
            "Embedding 1 size = ${embeddings[1].size}"
        )

        Log.d(
            "FaceEmbeddingTest",
            "Embedding 0 first values = ${
                embeddings[0]
                    .take(10)
                    .joinToString()
            }"
        )

        Log.d(
            "FaceEmbeddingTest",
            "Embedding 1 first values = ${
                embeddings[1]
                    .take(10)
                    .joinToString()
            }"
        )
    }
}