package com.example.workpulse.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workpulse.data.repository.CompOffApplicationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okio.IOException

@HiltWorker
class CompOffSyncWorker @AssistedInject constructor(
    @Assisted
    appContext : Context,

    @Assisted
    workerParams : WorkerParameters,

    private val compOffApplicationRepository: CompOffApplicationRepository

    )
    : CoroutineWorker(appContext,workerParams){
    override suspend fun doWork() : Result{

        return  try {
            compOffApplicationRepository.syncPendingCompOffApplications()
            Result.success()
        }catch (e : IOException){
            Result.retry()
        }catch (e : Exception){
            Result.failure()
        }
    }


}