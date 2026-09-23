package com.example.workpulse.feature.home.presentation.components.drawerRelated

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.workpulse.core.navigation.config.WorkPulseNavigationRegistry
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import com.example.workpulse.feature.config.domain.NavigationItem

@Composable
fun NavigationDrawerContent(

    employeeName: String,

    designation: String,
    company : String,

    onLogoutClick: () -> Unit,

    selectedRoute: String,
    navigationItems: List<NavigationItem>,
    onNavigationItemClick:(String) -> Unit


    ) {

    ModalDrawerSheet(

        modifier = Modifier
            .width(AdaptiveLayout.DrawerWidth)
            .fillMaxHeight(),

        drawerContainerColor = MaterialTheme.colorScheme.surface,

        drawerShape = RoundedCornerShape(
            topEnd = 28.dp,
            bottomEnd = 28.dp
        )

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            DrawerHeader(
                employeeName = employeeName,
                designation = designation,
                company = company
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(12.dp)
            )


            navigationItems.forEach{item->
                val destination = WorkPulseNavigationRegistry.resolve(item.navigationKey)
                if (destination != null){
                    DrawerMenuItem(
                        title = item.label,
                        icon = destination.icon,
                        selected = selectedRoute == item.navigationKey,
                        onClick = {
                            onNavigationItemClick(item.navigationKey)
                        }
                    )
                }
            }


            Spacer(
                modifier = Modifier.weight(1f)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            DrawerMenuItem(
                title = "Logout",
                icon = Icons.Outlined.Logout,
                onClick = onLogoutClick,
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Version 1.0.0",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

        }

    }

}
