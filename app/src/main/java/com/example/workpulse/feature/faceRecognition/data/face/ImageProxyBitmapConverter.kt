package com.example.workpulse.feature.faceRecognition.data.face

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream

class ImageProxyBitmapConverter {

    @OptIn(ExperimentalGetImage::class)
    fun toBitmap(
        imageProxy: ImageProxy
    ): Bitmap? {

        val image = imageProxy.image
            ?: return null

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(
            ySize + uSize + vSize
        )

        yBuffer.get(
            nv21,
            0,
            ySize
        )

        vBuffer.get(
            nv21,
            ySize,
            vSize
        )

        uBuffer.get(
            nv21,
            ySize + vSize,
            uSize
        )

        val yuvImage = android.graphics.YuvImage(
            nv21,
            android.graphics.ImageFormat.NV21,
            image.width,
            image.height,
            null
        )

        val outputStream =
            ByteArrayOutputStream()

        yuvImage.compressToJpeg(
            android.graphics.Rect(
                0,
                0,
                image.width,
                image.height
            ),
            100,
            outputStream
        )

        val jpegBytes =
            outputStream.toByteArray()

        return BitmapFactory.decodeByteArray(
            jpegBytes,
            0,
            jpegBytes.size
        )
    }
}