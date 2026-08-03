package com.example.workpulse.core.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workpulse.data.repository.LeaveApplicationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException

@HiltWorker
class LeaveSyncWorker @AssistedInject constructor(

    @Assisted
    appContext: Context,

    @Assisted
    workerParams: WorkerParameters,

    private val leaveApplicationRepository: LeaveApplicationRepository

) : CoroutineWorker(
    appContext,
    workerParams
) {

    override suspend fun doWork(): Result {

        return try {
            Log.d("LeaveSync", "LeaveSyncWorker Started")

            leaveApplicationRepository
                .syncPendingLeaveApplications()

            Result.success()

        } catch (e: IOException) {

            Result.retry()

        } catch (e: Exception) {

            Result.failure()

        }

    }

}