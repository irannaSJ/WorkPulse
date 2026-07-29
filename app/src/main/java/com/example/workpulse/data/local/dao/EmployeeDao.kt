package com.example.workpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workpulse.data.local.entity.EmployeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeEntity)

    @Update
    suspend fun updateEmployee(employee: EmployeeEntity)

    @Query("SELECT * FROM employee LIMIT 1")
    fun getEmployee(): Flow<EmployeeEntity?>

    @Query("DELETE FROM employee")
    suspend fun deleteEmployee()

    @Query("SELECT * FROM employee LIMIT 1")
    suspend fun getEmployeeOnce(): EmployeeEntity?


}