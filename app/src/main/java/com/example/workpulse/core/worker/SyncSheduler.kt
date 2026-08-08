package com.example.workpulse.core.worker

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

import java.util.concurrent.TimeUnit

class SyncScheduler(
    private val context: Context
) {

    fun scheduleAttendanceSync() {
        Log.d("AttendanceSync", "Scheduling WorkManager")


        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request =
            OneTimeWorkRequestBuilder<AttendanceSyncWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    10,
                    TimeUnit.SECONDS
                )
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                "attendance_sync",
                ExistingWorkPolicy.KEEP,
                request
            )
    }

    fun scheduleAttendanceRequestSync() {

        Log.d(
            "AttendanceRequestSync",
            "Scheduling attendance request sync"
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(
                NetworkType.CONNECTED
            )
            .build()

        val request =
            OneTimeWorkRequestBuilder<AttendanceRequestSyncWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    10,
                    TimeUnit.SECONDS
                )
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                "attendance_request_sync",
                ExistingWorkPolicy.KEEP,
                request
            )
    }


    fun scheduleLeaveSync() {

        Log.d(
            "LeaveSync",
            "Scheduling WorkManager"
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(
                NetworkType.CONNECTED
            )
            .build()

        val request =

            OneTimeWorkRequestBuilder<LeaveSyncWorker>()

                .setConstraints(constraints)

                .setBackoffCriteria(

                    BackoffPolicy.EXPONENTIAL,

                    10,

                    TimeUnit.SECONDS

                )

                .build()

        WorkManager
            .getInstance(context)

            .enqueueUniqueWork(

                "leave_sync",

                ExistingWorkPolicy.KEEP,

                request

            )

    }
}


