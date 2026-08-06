package com.example.workpulse.data.repository

import com.example.workpulse.data.local.dao.LeaveApplicationDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LeaveHistoryRepository @Inject constructor(

    private val leaveApplicationDao: LeaveApplicationDao

) {

    fun getLeaveApplications() =
        leaveApplicationDao.getAllLeaveApplications()

}