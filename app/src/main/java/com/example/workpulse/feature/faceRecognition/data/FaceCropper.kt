package com.example.workpulse.feature.faceRecognition.data

import android.graphics.Bitmap
import android.graphics.Rect
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class FaceCropper @Inject constructor() {

    fun cropFace(
        bitmap: Bitmap,
        boundingBox: Rect
    ): Bitmap? {

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

        if (width <= 0 || height <= 0) {
            return null
        }

        return Bitmap.createBitmap(
            bitmap,
            left,
            top,
            width,
            height
        )
    }
}