package com.example.workpulse.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workpulse.data.repository.AttendanceRequestRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class AttendanceRequestSyncWorker @AssistedInject constructor(

    @Assisted
    context: Context,

    @Assisted
    workerParameters: WorkerParameters,

    private val repository: AttendanceRequestRepository


    ) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            repository.syncPendingAttendanceRequests()
            Result.success()
        }catch (e: java.io.IOException){
            Result.retry()
        }catch (e : Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
}