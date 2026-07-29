package com.example.workpulse.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun AboutCard(

    appVersion: String = "1.0.0",

    companyName: String = "Datamann",

    appName: String = "WorkPulse"

) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )

    ) {

        Column(

            modifier = Modifier.padding(20.dp)

        ) {

            Row(

                verticalAlignment = Alignment.CenterVertically

            ) {

                Box(

                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color(0xFF2563EB).copy(alpha = 0.12f),
                            CircleShape
                        ),

                    contentAlignment = Alignment.Center

                ) {

                    Icon(

                        imageVector = Icons.Outlined.Info,

                        contentDescription = null,

                        tint = Color(0xFF2563EB)

                    )

                }

                Spacer(modifier = Modifier.size(16.dp))

                Column {

                    Text(

                        text = "About",

                        style = MaterialTheme.typography.titleLarge,

                        fontWeight = FontWeight.Bold

                    )

                    Text(

                        text = "Application information",

                        style = MaterialTheme.typography.bodyMedium,

                        color = Color.Gray

                    )

                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            AboutRow(
                title = "Application",
                value = appName
            )

            Spacer(modifier = Modifier.height(16.dp))

            AboutRow(
                title = "Version",
                value = appVersion
            )

            Spacer(modifier = Modifier.height(16.dp))

            AboutRow(
                title = "Developed By",
                value = companyName
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.Center,

                verticalAlignment = Alignment.CenterVertically

            ) {

                Icon(

                    imageVector = Icons.Outlined.PhoneAndroid,

                    contentDescription = null,

                    tint = Color.Gray,

                    modifier = Modifier.size(18.dp)

                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(

                    text = "Built with ❤️ using Jetpack Compose",

                    style = MaterialTheme.typography.bodySmall,

                    color = Color.Gray

                )

            }

        }

    }

}


@Composable
private fun AboutRow(

    title: String,

    value: String

) {

    Row(

        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceBetween,

        verticalAlignment = Alignment.CenterVertically

    ) {

        Text(

            text = title,

            style = MaterialTheme.typography.bodyLarge,

            color = Color.Gray

        )

        Text(

            text = value,

            style = MaterialTheme.typography.bodyLarge,

            fontWeight = FontWeight.SemiBold

        )

    }

}