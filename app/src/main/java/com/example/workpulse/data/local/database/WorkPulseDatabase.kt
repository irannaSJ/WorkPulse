package com.example.workpulse.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workpulse.data.local.dao.AttendanceRequestDao
import com.example.workpulse.data.local.dao.CompOffApplicationDao
import com.example.workpulse.data.local.dao.EmployeeDao
import com.example.workpulse.data.local.dao.LeaveApplicationDao
import com.example.workpulse.data.local.dao.LeaveBalanceDao
import com.example.workpulse.data.local.entity.EmployeeEntity
import com.example.workpulse.data.local.entity.AttendanceRequestEntity
import com.example.workpulse.data.local.entity.CompOffApplicationEntity
import com.example.workpulse.data.local.entity.LeaveApplicationEntity
import com.example.workpulse.data.local.entity.LeaveBalanceEntity
import com.example.workpulse.feature.attendance.data.local.dao.AttendanceDao
import com.example.workpulse.feature.attendance.data.local.entity.AttendanceEntity

@Database(
    entities = [
        EmployeeEntity::class,
        AttendanceEntity::class,
        AttendanceRequestEntity::class,
        LeaveBalanceEntity::class,
        LeaveApplicationEntity::class,
        CompOffApplicationEntity :: class
    ],
    version = 24,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class WorkPulseDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun attendanceDao() : AttendanceDao

    abstract fun attendanceRequestDao(): AttendanceRequestDao


    abstract fun leaveBalanceDao() : LeaveBalanceDao

    abstract fun leaveApplicationDao() : LeaveApplicationDao

    abstract fun compOffApplicationDao() : CompOffApplicationDao

}
