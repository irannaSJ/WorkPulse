package com.example.workpulse.data.repository

import com.example.workpulse.core.datastore.CookieStorage
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.core.network.CookieManager
import com.example.workpulse.data.local.entity.EmployeeEntity
import com.example.workpulse.data.remote.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val employeeRepository: EmployeeRepository,
    private val sessionManager: SessionManager,
    private val cookieManager: CookieManager,
    private val cookieStorage: CookieStorage
) {

    suspend fun login(
        username: String,
        password: String
    ): Result<Unit> = withContext(Dispatchers.IO) {

        try {

            val loginResponse = authApi.login(
                username = username,
                password = password
            )

            if (!loginResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Invalid username or password")
                )
            }

            val userResponse = authApi.getLoggedUser()

            if (!userResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Unable to fetch logged in user")
                )
            }

            val loggedUser = userResponse.body()?.message.orEmpty()

            val employeeResponse = authApi.getEmployee(
                filters = """[["user_id","=","$loggedUser"]]""",
                fields = """
                    [
                        "name",
                        "employee_name",
                        "user_id",
                        "company",
                        "department",
                        "designation",
                        "company_email",
                        "personal_email",
                        "cell_number",
                        "date_of_joining",
                        "current_address",
                        "image",
                        "leave_approver"
                    ]
                    """.trimIndent()
            )

            if (!employeeResponse.isSuccessful ||
                employeeResponse.body()?.data.isNullOrEmpty()
            ) {
                return@withContext Result.failure(
                    Exception("Employee not found")
                )
            }

            val employee = employeeResponse.body()!!.data.first()

            val employeeEntity = EmployeeEntity(
                employeeId = employee.name,
                userId = employee.userId,
                employeeName = employee.employeeName,
                company = employee.company,
                department = employee.department,
                designation = employee.designation,
                companyEmail = employee.companyEmail,
                personalEmail = employee.personalEmail,
                mobileNumber = employee.mobileNumber,
                profileImage = employee.profileImage,
                dateOfJoining = employee.dateOfJoining,
                currentAddress = employee.currentAddress,
                leaveApprover = employee.leaveApprover,
                updatedAt = System.currentTimeMillis()
            )

            employeeRepository.saveEmployee(employeeEntity)

            sessionManager.saveSession(
                employeeId = employee.name,
                userId = employee.userId,
                company = employee.company
            )

            Result.success(Unit)

        } catch (exception: Exception) {

            Result.failure(exception)
        }
    }

    suspend fun logout() {
        try {
            authApi.logout()
        }catch (e : Exception){}
        finally {
            employeeRepository.deleteEmployee()
            sessionManager.clearSession()
            cookieManager.clearCookies()
            cookieStorage.clear()
        }
    }
}