package com.example.workpulse.feature.faceRecognition.data.face

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject

class FaceEmbeddingModel @Inject constructor(
    private val context: Context
) {

    private var interpreter: Interpreter? = null

    fun initialize() {

        if (interpreter != null) {
            return
        }

        val model = loadModel()

        interpreter = Interpreter(model)

        Log.d(
            "FaceEmbeddingModel",
            "MobileFaceNet initialized"
        )

        Log.d(
            "FaceEmbeddingModel",
            "Input shape = ${
                interpreter!!
                    .getInputTensor(0)
                    .shape()
                    .contentToString()
            }"
        )

        Log.d(
            "FaceEmbeddingModel",
            "Output shape = ${
                interpreter!!
                    .getOutputTensor(0)
                    .shape()
                    .contentToString()
            }"
        )
    }

    fun generateEmbeddings(
        firstImage: FloatArray,
        secondImage: FloatArray
    ): Array<FloatArray> {

        check(interpreter != null) {
            "FaceEmbeddingModel is not initialized"
        }

        require(firstImage.size == 112 * 112 * 3) {
            "First image has invalid size"
        }

        require(secondImage.size == 112 * 112 * 3) {
            "Second image has invalid size"
        }

        val input = Array(2) { FloatArray(112 * 112 * 3) }

        input[0] = firstImage
        input[1] = secondImage

        val output = Array(2) {
            FloatArray(192)
        }

        interpreter!!.run(
            input,
            output
        )

        return output
    }



    fun generateEmbedding(
        image: FloatArray
    ): FloatArray {

        check(interpreter != null) {
            "FaceEmbeddingModel is not initialized"
        }

        require(image.size == 112 * 112 * 3) {
            "Image must contain exactly 112 × 112 × 3 values"
        }

        val input = Array(2) {
            FloatArray(112 * 112 * 3)
        }

        input[0] = image
        input[1] = image

        val output = Array(2) {
            FloatArray(192)
        }

        interpreter!!.run(
            input,
            output
        )

        return output[0]
    }

    fun close() {

        interpreter?.close()

        interpreter = null

        Log.d(
            "FaceEmbeddingModel",
            "MobileFaceNet closed"
        )
    }

    private fun loadModel(): MappedByteBuffer {

        val assetFileDescriptor =
            context.assets.openFd(
                "MobileFaceNet.tflite"
            )

        val inputStream =
            FileInputStream(
                assetFileDescriptor.fileDescriptor
            )

        val fileChannel =
            inputStream.channel

        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            assetFileDescriptor.startOffset,
            assetFileDescriptor.declaredLength
        )
    }
}