package com.example.workpulse.feature.home.presentation.components.drawerRelated

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.workpulse.R
import com.example.workpulse.core.ui.theme.Dimens

@Composable
fun DrawerHeader(
    employeeName: String,
    designation: String,
    company: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.Space24,
                vertical = Dimens.Space24
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.workpulse_logo),
            contentDescription = "WorkPulse Logo",
            modifier = Modifier.size(Dimens.HomeLogoHeight * 2)
        )

        Spacer(
            modifier = Modifier.height(Dimens.Space20)
        )

        Text(
            text = employeeName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(
            modifier = Modifier.height(Dimens.Space4)
        )

        Text(
            text = designation,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(Dimens.Space2)
        )

        Text(
            text = company,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

    }

}