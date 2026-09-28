//package com.example.workpulse.core.worker
//
//import android.content.Context
//import android.util.Log
//import androidx.work.BackoffPolicy
//import androidx.work.Constraints
//import androidx.work.ExistingWorkPolicy
//import androidx.work.NetworkType
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.WorkManager
//
//import java.util.concurrent.TimeUnit
//
//class SyncScheduler(
//    private val context: Context
//) {
//
//    fun scheduleAttendanceSync() {
//        Log.d("AttendanceSync", "Scheduling WorkManager")
//
//
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        val request =
//            OneTimeWorkRequestBuilder<AttendanceSyncWorker>()
//                .setConstraints(constraints)
//                .setBackoffCriteria(
//                    BackoffPolicy.EXPONENTIAL,
//                    10,
//                    TimeUnit.SECONDS
//                )
//                .build()
//
//        WorkManager
//            .getInstance(context)
//            .enqueueUniqueWork(
//                "attendance_sync",
//                ExistingWorkPolicy.KEEP,
//                request
//            )
//    }
//
//    fun scheduleAttendanceRequestSync() {
//
//        Log.d(
//            "AttendanceRequestSync",
//            "Scheduling attendance request sync"
//        )
//
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(
//                NetworkType.CONNECTED
//            )
//            .build()
//
//        val request =
//            OneTimeWorkRequestBuilder<AttendanceRequestSyncWorker>()
//                .setConstraints(constraints)
//                .setBackoffCriteria(
//                    BackoffPolicy.EXPONENTIAL,
//                    10,
//                    TimeUnit.SECONDS
//                )
//                .build()
//
//        WorkManager
//            .getInstance(context)
//            .enqueueUniqueWork(
//                "attendance_request_sync",
//                ExistingWorkPolicy.KEEP,
//                request
//            )
//    }
//
//
//    fun scheduleLeaveSync() {
//
//        Log.d(
//            "LeaveSync",
//            "Scheduling WorkManager"
//        )
//
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(
//                NetworkType.CONNECTED
//            )
//            .build()
//
//        val request =
//
//            OneTimeWorkRequestBuilder<LeaveSyncWorker>()
//
//                .setConstraints(constraints)
//
//                .setBackoffCriteria(
//
//                    BackoffPolicy.EXPONENTIAL,
//
//                    10,
//
//                    TimeUnit.SECONDS
//
//                )
//
//                .build()
//
//        WorkManager
//            .getInstance(context)
//
//            .enqueueUniqueWork(
//
//                "leave_sync",
//
//                ExistingWorkPolicy.KEEP,
//
//                request
//
//            )
//
//    }
//
//    fun scheduleCompOffSync(){
//        val  constraints = Constraints.Builder()
//            .setRequiredNetworkType(
//                NetworkType.CONNECTED
//            )
//            .build()
//        val request = OneTimeWorkRequestBuilder<CompOffSyncWorker>()
//            .setConstraints(constraints)
//            .setBackoffCriteria(
//                BackoffPolicy.EXPONENTIAL,
//                10,
//                TimeUnit.SECONDS
//            )
//            .build()
//
//        WorkManager.getInstance(context)
//            .enqueueUniqueWork(
//                "compoff_sync",
//                ExistingWorkPolicy.KEEP,
//                request
//
//            )
//    }
//}
//
//



package com.example.workpulse.core.worker

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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

    private var networkCallbackRegistered = false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            Log.d(
                "AttendanceSync",
                "Internet available → triggering attendance sync"
            )

            scheduleAttendanceSync()
        }
    }

    fun startInternetMonitoring() {

        if (networkCallbackRegistered) {
            return
        }

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

        val networkRequest =
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

        connectivityManager.registerNetworkCallback(
            networkRequest,
            networkCallback
        )

        networkCallbackRegistered = true

        Log.d(
            "AttendanceSync",
            "Internet monitoring started"
        )
    }

    fun scheduleAttendanceSync() {

        Log.d(
            "AttendanceSync",
            "Scheduling WorkManager"
        )

        val constraints =
            Constraints.Builder()
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

        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
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

        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
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

    fun scheduleCompOffSync() {

        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val request =
            OneTimeWorkRequestBuilder<CompOffSyncWorker>()
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
                "compoff_sync",
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}