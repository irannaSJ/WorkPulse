package com.example.workpulse.feature.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.workpulse.feature.profile.presentation.ProfileUiState
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.WorkPulseShapes
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import java.io.File

@Composable
fun ProfileHeader(
    uiState : ProfileUiState,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .widthIn(max = AdaptiveLayout.HomeColumnMaxWidth)
            .padding(horizontal = Dimens.Space16),
        shape = WorkPulseShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimens.Space24,
                    vertical = Dimens.Space32
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Surface(
                modifier = Modifier.size(Dimens.ProfileAvatar),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {


                if (uiState.employeeImage.isNotBlank()) {

                    AsyncImage(
                        model = File(uiState.employeeImage),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(Dimens.ProfileAvatar)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                } else {


                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .padding(Dimens.Space20)
                            .clip(CircleShape),
                        tint = MaterialTheme.colorScheme.primary
                    )

                }

            }

            Spacer(modifier = Modifier.height(Dimens.Space20))

            Text(
                text = uiState.employeeName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(Dimens.Space4))

            Text(
                text = uiState.designation,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Dimens.Space16))

            AssistChip(
                onClick = {},
                enabled = false,
                label = {
                    Text(uiState.employeeId)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Badge,
                        contentDescription = null
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    labelColor = MaterialTheme.colorScheme.primary,
                    leadingIconContentColor = MaterialTheme.colorScheme.primary
                )
            )

        }

    }

}
