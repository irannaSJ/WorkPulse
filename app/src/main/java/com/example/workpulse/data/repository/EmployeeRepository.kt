package com.example.workpulse.data.repository

import com.example.workpulse.data.local.dao.EmployeeDao
import com.example.workpulse.data.local.entity.EmployeeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeRepository @Inject constructor(
    private val employeeDao: EmployeeDao
){
    fun getEmployee(): Flow<EmployeeEntity?> {
        return employeeDao.getEmployee()
    }

    suspend fun saveEmployee(employee: EmployeeEntity) {
        employeeDao.insertEmployee(employee)
    }

    suspend fun updateEmployee(employee: EmployeeEntity) {
        employeeDao.updateEmployee(employee)
    }

    suspend fun deleteEmployee() {
        employeeDao.deleteEmployee()
    }

    suspend fun updateProfile(personalEmail : String,imageUri : String){
        val employee = employeeDao.getEmployeeOnce()?: return

        employeeDao.updateEmployee(
            employee.copy(
                personalEmail = personalEmail,
                profileImage = imageUri
            )
        )
    }
}