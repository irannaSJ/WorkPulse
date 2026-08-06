package com.example.workpulse.feature.profile.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LogoutCard(

    onLogoutClick: () -> Unit,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onLogoutClick
            ),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {

        Row(

            modifier = Modifier.padding(20.dp),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.Start

        ) {

            Icon(

                imageVector = Icons.Outlined.Logout,

                contentDescription = "Logout",

                modifier = Modifier.size(28.dp),

                tint = MaterialTheme.colorScheme.error

            )

            Spacer(modifier = Modifier.size(20.dp))

            Column {

                Text(

                    text = "Logout",

                    style = MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.SemiBold,

                    color = MaterialTheme.colorScheme.error

                )

                Text(

                    text = "Sign out from your WorkPulse account",

                    style = MaterialTheme.typography.bodyMedium,

                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )

            }

        }

    }

}