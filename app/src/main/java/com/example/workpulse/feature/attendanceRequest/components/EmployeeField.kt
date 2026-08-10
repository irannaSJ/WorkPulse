package com.example.workpulse.feature.attendanceRequest.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.workpulse.feature.leaveApplication.components.ReadOnlyField

@Composable
fun EmployeeField(

    employeeName: String,

    modifier: Modifier = Modifier

) {

    ReadOnlyField(

        modifier = modifier,

        label = "Employee",

        value = employeeName,

        placeholder = "Loading Employee...",

        leadingIcon = Icons.Outlined.Person

    )

}