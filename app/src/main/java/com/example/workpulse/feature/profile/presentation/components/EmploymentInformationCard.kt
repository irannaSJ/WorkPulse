package com.example.workpulse.feature.profile.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EmploymentInformationCard(

    employeeId: String,

    company: String,

    department: String,

    designation: String,

    joiningDate: String,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier.fillMaxWidth(),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(

                text = "Employment Information",

                style = MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.SemiBold

            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoRow(
                icon = Icons.Outlined.Badge,
                title = "Employee ID",
                value = employeeId
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                icon = Icons.Outlined.Business,
                title = "Company",
                value = company
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                icon = Icons.Outlined.Apartment,
                title = "Department",
                value = department
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                icon = Icons.Outlined.WorkOutline,
                title = "Designation",
                value = designation
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                icon = Icons.Outlined.CalendarMonth,
                title = "Date of Joining",
                value = joiningDate
            )

        }

    }

}