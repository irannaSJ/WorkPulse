package com.example.workpulse.feature.leaveApplication.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.theme.WorkPulseTheme

@Composable
fun BottomActionButtons(

    onResetClick: () -> Unit,

    onSaveClick: () -> Unit,

    isSubmitting: Boolean = false,

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

                text = "Actions",

                style = MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.SemiBold

            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(20.dp))

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(12.dp)

            ) {

                OutlinedButton(

                    onClick = onResetClick,

                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),

                    shape = RoundedCornerShape(16.dp),

                    enabled = !isSubmitting

                ) {

                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Reset")

                }

                Button(

                    onClick = onSaveClick,

                    modifier = Modifier
                        .weight(1.5f)
                        .height(56.dp),

                    shape = RoundedCornerShape(16.dp),

                    enabled = !isSubmitting

                ) {

                    if (isSubmitting) {

                        CircularProgressIndicator(

                            modifier = Modifier.size(22.dp),

                            strokeWidth = 2.dp,

                            color = MaterialTheme.colorScheme.onPrimary

                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text("Submitting...")

                    } else {

                        Icon(

                            imageVector = Icons.Outlined.Save,

                            contentDescription = null

                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Save Application")

                    }

                }

            }

        }

    }

}


