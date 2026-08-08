package com.example.workpulse.core.worker

import android.content.Context
import android.util.Log
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

        Log.d("Attendance Sync Worker Started","Attendance Request Worker Started")

        return try {

            Log.d("Attendance Request","Calling Attendance request sync Worker")

            repository.syncPendingAttendanceRequests()
            repository.syncLatestAttendanceRequests()

            Log.d(
                "AttendanceRequestSync",
                "Worker COMPLETED SUCCESSFULLY"
            )
            Result.success()
        }catch (e: java.io.IOException){


            Log.e(
                "AttendanceRequestSync",
                "Network error - retrying",
                e
            )

            Result.retry()
        }catch (e : Exception){


            Log.e(
                "AttendanceRequestSync",
                "Worker failed",
                e
            )
            e.printStackTrace()
            Result.failure()
        }
    }
}