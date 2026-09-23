package com.example.workpulse.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workpulse.data.local.dao.AttendanceRequestDao
import com.example.workpulse.data.local.dao.CompOffApplicationDao
import com.example.workpulse.data.local.dao.EmployeeDao
import com.example.workpulse.data.local.dao.FaceEmbeddingDao
import com.example.workpulse.data.local.dao.LeaveApplicationDao
import com.example.workpulse.data.local.dao.LeaveBalanceDao
import com.example.workpulse.data.local.dao.config.FeatureDao
import com.example.workpulse.data.local.dao.config.HomeSectionDao
import com.example.workpulse.data.local.dao.config.NavigationItemDao
import com.example.workpulse.data.local.dao.config.QuickActionDao
import com.example.workpulse.data.local.dao.config.WorkPulseConfigDao
import com.example.workpulse.data.local.dao.config.WorkPulseThemeDao
import com.example.workpulse.data.local.entity.EmployeeEntity
import com.example.workpulse.data.local.entity.AttendanceRequestEntity
import com.example.workpulse.data.local.entity.CompOffApplicationEntity
import com.example.workpulse.data.local.entity.FaceEmbeddingEntity
import com.example.workpulse.data.local.entity.LeaveApplicationEntity
import com.example.workpulse.data.local.entity.LeaveBalanceEntity
import com.example.workpulse.data.local.entity.configEntity.FeatureEntity
import com.example.workpulse.data.local.entity.configEntity.HomeSectionEntity
import com.example.workpulse.data.local.entity.configEntity.NavigationItemEntity
import com.example.workpulse.data.local.entity.configEntity.QuickActionEntity
import com.example.workpulse.data.local.entity.configEntity.WorkPulseConfigEntity
import com.example.workpulse.data.local.entity.configEntity.WorkPulseThemeEntity
import com.example.workpulse.feature.attendance.data.local.dao.AttendanceDao
import com.example.workpulse.feature.attendance.data.local.entity.AttendanceEntity

@Database(
    entities = [
        EmployeeEntity::class,
        AttendanceEntity::class,
        AttendanceRequestEntity::class,
        LeaveBalanceEntity::class,
        LeaveApplicationEntity::class,
        CompOffApplicationEntity :: class,
        FaceEmbeddingEntity::class,

        WorkPulseConfigEntity::class,
        WorkPulseThemeEntity::class,
        NavigationItemEntity::class,
        QuickActionEntity::class,
        HomeSectionEntity::class,
        FeatureEntity::class,
    ],
    version = 28,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class WorkPulseDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun attendanceDao() : AttendanceDao

    abstract fun attendanceRequestDao(): AttendanceRequestDao


    abstract fun leaveBalanceDao() : LeaveBalanceDao

    abstract fun leaveApplicationDao() : LeaveApplicationDao
    abstract fun faceEmbeddingDao() :  FaceEmbeddingDao

    abstract fun compOffApplicationDao() : CompOffApplicationDao


    //Related to Configuration of WorkPulse from ERPNext
    abstract fun workPulseConfigDao(): WorkPulseConfigDao
    abstract fun workPulseThemeDao(): WorkPulseThemeDao
    abstract fun navigationItemDao(): NavigationItemDao
    abstract fun quickActionDao(): QuickActionDao
    abstract fun homeSectionDao(): HomeSectionDao
    abstract fun featureDao(): FeatureDao

}
