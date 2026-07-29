package com.example.workpulse.core.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workpulse.data.repository.AttendanceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException

@HiltWorker
class AttendanceSyncWorker @AssistedInject constructor(

    @Assisted appContext: Context,

    @Assisted workerParams: WorkerParameters,

    private val attendanceRepository: AttendanceRepository

) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        return try {

            attendanceRepository.syncPendingAttendance()

            Result.success()

        } catch (e: java.io.IOException) {

            // Temporary problem (no internet / server unavailable)
            // Keep pending records and let WorkManager retry.
            Result.retry()

        } catch (e: Exception) {

            e.printStackTrace()

            // Unexpected error
            Result.failure()

        }
    }
}