package com.example.workpulse.feature.faceRecognition.data.face

import android.graphics.Bitmap

class FaceTensorConverter {

    companion object {
        private const val IMAGE_SIZE = 112
        private const val CHANNELS = 3
    }

    fun bitmapToFloatArray(
        bitmap: Bitmap
    ): FloatArray {

        require(
            bitmap.width == IMAGE_SIZE &&
                    bitmap.height == IMAGE_SIZE
        ) {
            "Bitmap must be 112 x 112"
        }

        val pixels = IntArray(
            IMAGE_SIZE * IMAGE_SIZE
        )

        bitmap.getPixels(
            pixels,
            0,
            IMAGE_SIZE,
            0,
            0,
            IMAGE_SIZE,
            IMAGE_SIZE
        )

        val input = FloatArray(
            IMAGE_SIZE *
                    IMAGE_SIZE *
                    CHANNELS
        )

        var index = 0

        for (pixel in pixels) {

            val red = (pixel shr 16) and 0xFF
            val green = (pixel shr 8) and 0xFF
            val blue = pixel and 0xFF

            input[index++] =
                (red - 127.5f) / 128.0f

            input[index++] =
                (green - 127.5f) / 128.0f

            input[index++] =
                (blue - 127.5f) / 128.0f
        }

        return input
    }
}