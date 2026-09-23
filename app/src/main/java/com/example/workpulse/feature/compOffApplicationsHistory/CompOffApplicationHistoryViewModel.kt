package com.example.workpulse.feature.compOffApplicationsHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.core.worker.SyncScheduler
import com.example.workpulse.data.local.entity.ApplicationStatus
import com.example.workpulse.data.local.entity.CompOffApplicationEntity
import com.example.workpulse.data.local.entity.EmployeeEntity
import com.example.workpulse.data.repository.CompOffApplicationHistoryRepository
import com.example.workpulse.data.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CompOffApplicationHistoryViewModel @Inject constructor(
    private val repository: CompOffApplicationHistoryRepository,
    private val employeeRepository : EmployeeRepository,
    private val syncScheduler: SyncScheduler
): ViewModel(){
    private val _uiState = MutableStateFlow(CompOffApplicationHistoryUiState())
    val uiState : StateFlow<CompOffApplicationHistoryUiState> = _uiState.asStateFlow()

    private var allApplications = emptyList<CompOffApplicationHistoryCardUi>()
    private var employee : EmployeeEntity? = null

    init {
        observeCompOffApplications()
        observeEmployee()
        syncScheduler.scheduleCompOffSync()
    }

    private fun observeEmployee() {

        viewModelScope.launch {

            employeeRepository
                .getEmployee()
                .collect { employeeEntity ->

                    employee = employeeEntity

                }

        }

    }
    private fun observeCompOffApplications(){
        viewModelScope.launch {
            repository
                .getCompOffApplications()
                .collect {
                    applications ->
                    val cards = applications.map {
                        it.toCompOffHistoryCardUi()
                    }
                    allApplications = cards

                    _uiState.update {
                        it.copy(
                            compOffApplication = cards,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    applyFilters()
                }
        }

    }



    private fun applyFilters(){
        val selectedFilter = _uiState.value.selectedFilter
        val filteredList = allApplications.filter { leave ->
            val matchedFilter = when(selectedFilter){
                CompOffApplicationHistoryFilter.ALL -> true
                CompOffApplicationHistoryFilter.APPROVED -> leave.status == ApplicationStatus.SUBMITTED
                CompOffApplicationHistoryFilter.PENDING -> leave.status == ApplicationStatus.PENDING
                CompOffApplicationHistoryFilter.REJECTED -> leave.status == ApplicationStatus.REJECTED
                CompOffApplicationHistoryFilter.CANCELLED -> leave.status == ApplicationStatus.CANCELLED
            }
            matchedFilter
        }
        _uiState.update {
            it.copy(
                compOffApplication = filteredList
            )
        }
    }

    fun onFilterChanged(filter : CompOffApplicationHistoryFilter){
        _uiState.update {
            it.copy(
                selectedFilter = filter
            )
        }
        applyFilters()
    }


    fun onCompOffClicked(
        compOff : CompOffApplicationHistoryCardUi
    ){
        val employee = employee ?:return
        _uiState.update {
            it.copy(
                compOffDetails = CompOffDetailsUi(
                    id = compOff.id,
                    employeeName = employee.employeeName,
                    erpNextId = compOff.erpNextId,
                    fromDate = compOff.fromDate,
                    toDate = compOff.toDate,
                    reason = compOff.reason,
                    appliedOn = compOff.appliedOn,
                    status = compOff.status
                ),
                showBottomSheet = true
            )
        }
    }

    fun hideBottomSheet(){
        _uiState.update {
            it.copy(
                compOffDetails = null,
                showBottomSheet = false
            )
        }
    }
}



private fun CompOffApplicationEntity.toCompOffHistoryCardUi(): CompOffApplicationHistoryCardUi {

    return CompOffApplicationHistoryCardUi(

        id = id,
        reason = reason,

        fromDate = formatDate(fromDate),

        toDate = formatDate(toDate),

        status = compOffApplicationStatus,
        erpNextId = erpNextId,
        appliedOn = "",
        )

}

private fun formatDate(time: Long): String {

    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    return formatter.format(Date(time))

}