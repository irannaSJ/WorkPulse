package com.example.workpulse.di

import android.content.Context
import androidx.room.Room
import com.example.workpulse.data.local.dao.AttendanceRequestDao
import com.example.workpulse.data.local.dao.CompOffApplicationDao
import com.example.workpulse.data.local.dao.EmployeeDao
import com.example.workpulse.data.local.dao.LeaveApplicationDao
import com.example.workpulse.data.local.dao.LeaveBalanceDao
import com.example.workpulse.data.local.database.MIGRATION_23_24
import com.example.workpulse.data.local.database.WorkPulseDatabase
import com.example.workpulse.feature.attendance.data.local.dao.AttendanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWorkPulseDatabase(
        @ApplicationContext context: Context
    ): WorkPulseDatabase {

        return Room.databaseBuilder(
            context,
            WorkPulseDatabase::class.java,
            "workpulse_database"
        )
            .addMigrations(MIGRATION_23_24)
            .build()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(
        database: WorkPulseDatabase
    ): EmployeeDao {
        return database.employeeDao()
    }

    @Provides
    @Singleton
    fun provideAttendanceDao(
        database: WorkPulseDatabase
    ): AttendanceDao{
        return database.attendanceDao()
    }

    @Provides
    @Singleton
    fun provideAttendanceRequestDao(
        database: WorkPulseDatabase
    ): AttendanceRequestDao {
        return database.attendanceRequestDao()
    }


    @Provides
    @Singleton
    fun provideLeaveBalanceDao(
        database: WorkPulseDatabase
    ): LeaveBalanceDao {
        return database.leaveBalanceDao()
    }

    @Provides
    @Singleton
    fun provideCompOffApplicationDao(
        database : WorkPulseDatabase
    ): CompOffApplicationDao{
        return  database.compOffApplicationDao()
    }

    @Provides
    @Singleton
    fun provideLeaveApplicationDao(
        database: WorkPulseDatabase
    ): LeaveApplicationDao{
        return database.leaveApplicationDao()
    }
}
