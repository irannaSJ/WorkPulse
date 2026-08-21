package com.example.workpulse.feature.faceRecognition.data.face

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class FaceModelInspector(
    private val context: Context
) {

    fun inspect() {

        val model = loadModel()

        val interpreter = Interpreter(model)

        Log.d(
            "FaceModelInspector",
            "Input tensor count = ${interpreter.inputTensorCount}"
        )

        Log.d(
            "FaceModelInspector",
            "Output tensor count = ${interpreter.outputTensorCount}"
        )

        for (index in 0 until interpreter.inputTensorCount) {

            val tensor = interpreter.getInputTensor(index)

            Log.d(
                "FaceModelInspector",
                """
                INPUT[$index]
                name=${tensor.name()}
                shape=${tensor.shape().contentToString()}
                dataType=${tensor.dataType()}
                """.trimIndent()
            )
        }

        for (index in 0 until interpreter.outputTensorCount) {

            val tensor = interpreter.getOutputTensor(index)

            Log.d(
                "FaceModelInspector",
                """
                OUTPUT[$index]
                name=${tensor.name()}
                shape=${tensor.shape().contentToString()}
                dataType=${tensor.dataType()}
                """.trimIndent()
            )
        }

        interpreter.close()
    }

    private fun loadModel(): MappedByteBuffer {

        val assetFileDescriptor =
            context.assets.openFd("MobileFaceNet.tflite")

        val inputStream =
            FileInputStream(assetFileDescriptor.fileDescriptor)

        val fileChannel = inputStream.channel

        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            assetFileDescriptor.startOffset,
            assetFileDescriptor.declaredLength
        )
    }
}