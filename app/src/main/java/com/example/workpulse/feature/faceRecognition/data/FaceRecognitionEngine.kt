package com.example.workpulse.feature.faceRecognition.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.sqrt
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class FaceRecognitionEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {

        private const val TAG = "FaceRecognitionEngine"

        private const val MODEL_NAME =
            "MobileFaceNet.tflite"

        private const val INPUT_SIZE = 112

        private const val CHANNELS = 3

        private const val MODEL_BATCH_SIZE = 2

        private const val EMBEDDING_SIZE = 192

        private const val IMAGE_MEAN = 127.5f

        private const val IMAGE_STD = 128f

        private const val MIN_FACE_SIZE = 80
    }

    private var interpreter: Interpreter? = null

    @Volatile
    private var faceDetector: com.google.mlkit.vision.face.FaceDetector? = null

    @Synchronized
    private fun detector(): com.google.mlkit.vision.face.FaceDetector {
        return faceDetector ?: FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(
                    FaceDetectorOptions.PERFORMANCE_MODE_FAST
                )
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(
                    FaceDetectorOptions.CLASSIFICATION_MODE_ALL
                )
                .build()
        ).also { faceDetector = it }
    }

    // ------------------------------------------------------------------------
    // MODEL
    // ------------------------------------------------------------------------

    fun initialize() {

        detector()
        if (interpreter != null) return

        val model = loadModelFile()

        interpreter = Interpreter(
            model,
            Interpreter.Options().apply {
                numThreads = 4
            }
        )

        val inputTensor =
            interpreter!!.getInputTensor(0)

        val outputTensor =
            interpreter!!.getOutputTensor(0)

        Log.d(
            TAG,
            "MobileFaceNet initialized"
        )

        Log.d(
            TAG,
            "Input shape = ${
                inputTensor.shape().contentToString()
            }"
        )

        Log.d(
            TAG,
            "Input type = ${inputTensor.dataType()}"
        )

        Log.d(
            TAG,
            "Input bytes = ${inputTensor.numBytes()}"
        )

        Log.d(
            TAG,
            "Output shape = ${
                outputTensor.shape().contentToString()
            }"
        )

        Log.d(
            TAG,
            "Output type = ${outputTensor.dataType()}"
        )

        Log.d(
            TAG,
            "Output bytes = ${outputTensor.numBytes()}"
        )
    }

    suspend fun getEmbedding(
        faceBitmap: Bitmap
    ): FloatArray =
        withContext(Dispatchers.Default) {

            check(interpreter != null) {
                "FaceRecognitionEngine is not initialized"
            }

            val resizedBitmap =
                Bitmap.createScaledBitmap(
                    faceBitmap,
                    INPUT_SIZE,
                    INPUT_SIZE,
                    true
                )

            try {

                val inputBuffer =
                    bitmapToInputBuffer(
                        resizedBitmap
                    )

                val output =
                    Array(MODEL_BATCH_SIZE) {
                        FloatArray(
                            EMBEDDING_SIZE
                        )
                    }

                interpreter!!.run(
                    inputBuffer,
                    output
                )
                val normalized =
                    l2Normalize(output[0])

                return@withContext normalized

            } finally {

                if (resizedBitmap !== faceBitmap) {
                    resizedBitmap.recycle()
                }
            }
        }

    // ------------------------------------------------------------------------
    // CAMERA FRAME → FACE
    // ------------------------------------------------------------------------

    /**
     * Processes one complete CameraX frame.
     *
     * The ImageProxy is closed exactly once inside this method.
     *
     * Result:
     *     cropped face Bitmap
     *     or null when no usable face exists.
     */
    @OptIn(ExperimentalGetImage::class)
    suspend fun analyzeFrame(
        imageProxy: ImageProxy
    ): FaceFrame = withContext(Dispatchers.Default) {

        try {

            val mediaImage =
                imageProxy.image
                    ?: return@withContext FaceFrame(0, null, null, 0, 0)

            val rotationDegrees =
                imageProxy.imageInfo.rotationDegrees

            val inputImage =
                InputImage.fromMediaImage(
                    mediaImage,
                    rotationDegrees
                )

            val faces =
                detector()
                    .process(inputImage)
                    .await()

            if (faces.size != 1) {
                return@withContext FaceFrame(faces.size, null, null, 0, 0)
            }

            val face =
                faces.first()

            val bitmap =
                imageProxyToBitmap(
                    imageProxy
                )
                    ?: return@withContext FaceFrame(1, face, null, 0, 0)

            val rotatedBitmap =
                rotateBitmap(
                    bitmap,
                    rotationDegrees
                )

            try {

                val adjustedBoundingBox =
                    adjustBoundingBoxForRotation(
                        face.boundingBox,
                        imageProxy.width,
                        imageProxy.height,
                        rotationDegrees
                    )

                FaceFrame(
                    faceCount = 1,
                    face = face,
                    bitmap = alignedFace(
                        face = face,
                        rotatedBitmap = rotatedBitmap,
                        originalWidth = imageProxy.width,
                        originalHeight = imageProxy.height,
                        rotationDegrees = rotationDegrees
                    ) ?: cropFace(rotatedBitmap, adjustedBoundingBox),
                    frameWidth = rotatedBitmap.width,
                    frameHeight = rotatedBitmap.height
                )

            } finally {

                if (rotatedBitmap !== bitmap) {
                    rotatedBitmap.recycle()
                }

                bitmap.recycle()
            }

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "Face extraction failed",
                exception
            )

            throw exception

        } finally {

            imageProxy.close()
        }
    }

    data class FaceFrame(
        val faceCount: Int,
        val face: Face?,
        val bitmap: Bitmap?,
        val frameWidth: Int,
        val frameHeight: Int
    )

    /** Retained for the development-only model test screen. Production uses analyzeFrame. */
    suspend fun extractFaceFromFrame(imageProxy: ImageProxy): Bitmap? = analyzeFrame(imageProxy).bitmap

    /**
     * Crops a detected face from a bitmap.
     */
    fun cropFace(
        bitmap: Bitmap,
        boundingBox: Rect
    ): Bitmap? {

        val left =
            boundingBox.left
                .coerceAtLeast(0)

        val top =
            boundingBox.top
                .coerceAtLeast(0)

        val right =
            boundingBox.right
                .coerceAtMost(bitmap.width)

        val bottom =
            boundingBox.bottom
                .coerceAtMost(bitmap.height)

        val width =
            right - left

        val height =
            bottom - top

        if (
            width < MIN_FACE_SIZE ||
            height < MIN_FACE_SIZE
        ) {
            Log.d(
                TAG,
                "Face too small: ${width}x$height"
            )

            return null
        }

        if (
            width <= 0 ||
            height <= 0
        ) {
            return null
        }

        Log.d(
            TAG,
            "Cropped face = ${width}x$height"
        )

        return Bitmap.createBitmap(
            bitmap,
            left,
            top,
            width,
            height
        )
    }

    /** Aligns eye positions to MobileFaceNet's expected canonical face orientation. */
    private fun alignedFace(
        face: Face,
        rotatedBitmap: Bitmap,
        originalWidth: Int,
        originalHeight: Int,
        rotationDegrees: Int
    ): Bitmap? {
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position ?: return null
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position ?: return null
        val left = rotatePoint(leftEye, originalWidth, originalHeight, rotationDegrees)
        val right = rotatePoint(rightEye, originalWidth, originalHeight, rotationDegrees)
        if (kotlin.math.hypot((right.x - left.x).toDouble(), (right.y - left.y).toDouble()) < 20.0) return null

        val transform = Matrix()
        val source = floatArrayOf(left.x, left.y, right.x, right.y)
        val destination = floatArrayOf(35f, 43f, 77f, 43f)
        if (!transform.setPolyToPoly(source, 0, destination, 0, 2)) return null
        return Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888).also { output ->
            Canvas(output).drawBitmap(rotatedBitmap, transform, null)
        }
    }

    private fun rotatePoint(point: PointF, originalWidth: Int, originalHeight: Int, rotationDegrees: Int): PointF =
        when (rotationDegrees) {
            90 -> PointF(originalHeight - point.y, point.x)
            180 -> PointF(originalWidth - point.x, originalHeight - point.y)
            270 -> PointF(point.y, originalWidth - point.x)
            else -> PointF(point.x, point.y)
        }

    // ------------------------------------------------------------------------
    // EMBEDDING COMPARISON
    // ------------------------------------------------------------------------

    fun cosineSimilarity(
        first: FloatArray,
        second: FloatArray
    ): Float {

        require(
            first.size == second.size
        ) {
            "Embedding sizes must match"
        }

        var dotProduct = 0.0
        var firstMagnitude = 0.0
        var secondMagnitude = 0.0

        for (i in first.indices) {

            val a =
                first[i].toDouble()

            val b =
                second[i].toDouble()

            dotProduct += a * b

            firstMagnitude += a * a

            secondMagnitude += b * b
        }

        val denominator =
            sqrt(firstMagnitude) *
                    sqrt(secondMagnitude)

        if (denominator == 0.0) {
            return -1f
        }

        return (
                dotProduct / denominator
                ).toFloat()
    }

    fun averageAndNormalize(
        embeddings: List<FloatArray>
    ): FloatArray {

        require(
            embeddings.isNotEmpty()
        ) {
            "Embeddings cannot be empty"
        }

        val size =
            embeddings.first().size

        require(
            embeddings.all {
                it.size == size
            }
        ) {
            "All embeddings must have the same size"
        }

        val average =
            FloatArray(size)

        for (embedding in embeddings) {

            for (i in embedding.indices) {
                average[i] += embedding[i]
            }
        }

        for (i in average.indices) {
            average[i] /= embeddings.size
        }

        return l2Normalize(average)
    }

    // ------------------------------------------------------------------------
    // IMAGE PREPROCESSING
    // ------------------------------------------------------------------------

    private fun bitmapToInputBuffer(
        bitmap: Bitmap
    ): ByteBuffer {

        val bufferSize =
            MODEL_BATCH_SIZE *
                    INPUT_SIZE *
                    INPUT_SIZE *
                    CHANNELS *
                    Float.SIZE_BYTES

        val buffer =
            ByteBuffer.allocateDirect(
                bufferSize
            )

        buffer.order(
            ByteOrder.nativeOrder()
        )

        val pixels =
            IntArray(
                INPUT_SIZE *
                        INPUT_SIZE
            )

        bitmap.getPixels(
            pixels,
            0,
            INPUT_SIZE,
            0,
            0,
            INPUT_SIZE,
            INPUT_SIZE
        )

        /*
         * MobileFaceNet expects a fixed batch of 2.
         * The same face is therefore placed into both
         * batch slots.
         */
        repeat(MODEL_BATCH_SIZE) {

            for (pixel in pixels) {

                val r =
                    ((pixel shr 16) and 0xFF)
                        .toFloat()

                val g =
                    ((pixel shr 8) and 0xFF)
                        .toFloat()

                val b =
                    (pixel and 0xFF)
                        .toFloat()

                buffer.putFloat(
                    (r - IMAGE_MEAN) /
                            IMAGE_STD
                )

                buffer.putFloat(
                    (g - IMAGE_MEAN) /
                            IMAGE_STD
                )

                buffer.putFloat(
                    (b - IMAGE_MEAN) /
                            IMAGE_STD
                )
            }
        }

        buffer.rewind()

        return buffer
    }

    private fun l2Normalize(
        embedding: FloatArray
    ): FloatArray {

        var sum = 0.0

        for (value in embedding) {

            sum +=
                value.toDouble() *
                        value.toDouble()
        }

        val magnitude =
            sqrt(sum)

        if (magnitude == 0.0) {
            return embedding
        }

        for (i in embedding.indices) {

            embedding[i] =
                (
                        embedding[i] /
                                magnitude
                        ).toFloat()
        }

        return embedding
    }

    // ------------------------------------------------------------------------
    // CAMERA IMAGE CONVERSION
    // ------------------------------------------------------------------------

    /**
     * Converts CameraX YUV_420_888 into an RGB Bitmap.
     *
     * We use the three ImageProxy planes directly so that
     * rowStride and pixelStride are handled correctly.
     */
    @OptIn(ExperimentalGetImage::class)
    private fun imageProxyToBitmap(
        imageProxy: ImageProxy
    ): Bitmap? {

        val image =
            imageProxy.image
                ?: return null

        val width =
            image.width

        val height =
            image.height

        val yPlane =
            image.planes[0]

        val uPlane =
            image.planes[1]

        val vPlane =
            image.planes[2]

        val yBuffer =
            yPlane.buffer

        val uBuffer =
            uPlane.buffer

        val vBuffer =
            vPlane.buffer

        yBuffer.rewind()
        uBuffer.rewind()
        vBuffer.rewind()

        val yRowStride =
            yPlane.rowStride

        val uRowStride =
            uPlane.rowStride

        val vRowStride =
            vPlane.rowStride

        val uPixelStride =
            uPlane.pixelStride

        val vPixelStride =
            vPlane.pixelStride

        val pixels =
            IntArray(width * height)

        var outputIndex = 0

        for (y in 0 until height) {

            val uvRow =
                y / 2

            for (x in 0 until width) {

                val uvColumn =
                    x / 2

                val yIndex =
                    y * yRowStride + x

                val uIndex =
                    uvRow * uRowStride +
                            uvColumn * uPixelStride

                val vIndex =
                    uvRow * vRowStride +
                            uvColumn * vPixelStride

                val yValue =
                    yBuffer.get(
                        yIndex
                    ).toInt() and 0xFF

                val uValue =
                    uBuffer.get(
                        uIndex
                    ).toInt() and 0xFF

                val vValue =
                    vBuffer.get(
                        vIndex
                    ).toInt() and 0xFF

                val yAdjusted =
                    yValue - 16

                val uAdjusted =
                    uValue - 128

                val vAdjusted =
                    vValue - 128

                var r =
                    (1.164f * yAdjusted) +
                            (1.596f * vAdjusted)

                var g =
                    (1.164f * yAdjusted) -
                            (0.392f * uAdjusted) -
                            (0.813f * vAdjusted)

                var b =
                    (1.164f * yAdjusted) +
                            (2.017f * uAdjusted)

                r =
                    r.coerceIn(
                        0f,
                        255f
                    )

                g =
                    g.coerceIn(
                        0f,
                        255f
                    )

                b =
                    b.coerceIn(
                        0f,
                        255f
                    )

                pixels[outputIndex++] =
                    (
                            (0xFF shl 24) or
                                    (r.toInt() shl 16) or
                                    (g.toInt() shl 8) or
                                    b.toInt()
                            )
            }
        }

        return Bitmap.createBitmap(
            pixels,
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
    }

    private fun rotateBitmap(
        bitmap: Bitmap,
        rotationDegrees: Int
    ): Bitmap {

        if (rotationDegrees == 0) {
            return bitmap
        }

        val matrix =
            Matrix()

        matrix.postRotate(
            rotationDegrees.toFloat()
        )

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    /**
     * ML Kit's bounding box uses the coordinate system
     * of the InputImage. After rotation, the bitmap has
     * potentially different dimensions, so we map the
     * rectangle into the rotated bitmap coordinate space.
     */
    private fun adjustBoundingBoxForRotation(
        box: Rect,
        originalWidth: Int,
        originalHeight: Int,
        rotationDegrees: Int
    ): Rect {

        return when (rotationDegrees) {

            0 -> box

            90 -> Rect(
                originalHeight - box.bottom,
                box.left,
                originalHeight - box.top,
                box.right
            )

            180 -> Rect(
                originalWidth - box.right,
                originalHeight - box.bottom,
                originalWidth - box.left,
                originalHeight - box.top
            )

            270 -> Rect(
                box.top,
                originalWidth - box.right,
                box.bottom,
                originalWidth - box.left
            )

            else -> box
        }
    }

    private fun loadModelFile():
            MappedByteBuffer {

        val fileDescriptor =
            context.assets.openFd(
                MODEL_NAME
            )

        FileInputStream(
            fileDescriptor.fileDescriptor
        ).use { inputStream ->

            val channel =
                inputStream.channel

            return channel.map(
                FileChannel.MapMode.READ_ONLY,
                fileDescriptor.startOffset,
                fileDescriptor.declaredLength
            )
        }
    }

    fun close() {

        faceDetector?.close()
        faceDetector = null

        interpreter?.close()

        interpreter = null

        Log.d(
            TAG,
            "FaceRecognitionEngine closed"
        )
    }
}
