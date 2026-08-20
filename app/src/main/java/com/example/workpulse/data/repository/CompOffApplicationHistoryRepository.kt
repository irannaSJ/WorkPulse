package com.example.workpulse.data.repository

import com.example.workpulse.data.local.dao.CompOffApplicationDao
import javax.inject.Inject


class CompOffApplicationHistoryRepository @Inject constructor(
    private val compOffApplicationDao: CompOffApplicationDao
) {
    fun getCompOffApplications() = compOffApplicationDao.getAllCompOffApplications()
}