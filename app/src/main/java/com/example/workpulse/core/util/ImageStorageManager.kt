package com.example.workpulse.core.util

import android.content.Context
import android.net.Uri
import java.io.File

object ImageStorageManager {

    fun saveProfileImage(
        context: Context,
        uri: Uri,
        employeeId: String
    ): String {

        val directory = File(context.filesDir, "profile_images")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val imageFile = File(directory, "${employeeId}.jpg")

        context.contentResolver.openInputStream(uri)?.use { input ->
            imageFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return imageFile.absolutePath
    }
}