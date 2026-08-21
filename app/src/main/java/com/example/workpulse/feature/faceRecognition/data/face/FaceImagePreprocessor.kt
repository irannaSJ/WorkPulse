package com.example.workpulse.feature.faceRecognition.data.face

import android.graphics.Bitmap
import android.graphics.Rect
import kotlin.math.max
import kotlin.math.min

class FaceImagePreprocessor {

    fun cropAndResize(
        bitmap: Bitmap,
        boundingBox: Rect
    ): Bitmap {

        val left = max(
            0,
            boundingBox.left
        )

        val top = max(
            0,
            boundingBox.top
        )

        val right = min(
            bitmap.width,
            boundingBox.right
        )

        val bottom = min(
            bitmap.height,
            boundingBox.bottom
        )

        val width = right - left
        val height = bottom - top

        require(
            width > 0 && height > 0
        ) {
            "Invalid face bounding box"
        }

        val croppedBitmap = Bitmap.createBitmap(
            bitmap,
            left,
            top,
            width,
            height
        )

        return Bitmap.createScaledBitmap(
            croppedBitmap,
            112,
            112,
            true
        )
    }
}