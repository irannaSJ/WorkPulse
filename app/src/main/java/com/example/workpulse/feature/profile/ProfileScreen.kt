package com.example.workpulse.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workpulse.R
import com.example.workpulse.feature.profile.components.AboutCard
import com.example.workpulse.feature.profile.components.MenuSection
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
//import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workpulse.feature.profile.components.EditProfileBottomSheet
import java.io.File

@Composable
fun ProfileScreen(

    uiState: ProfileUiState,

    onBackClick: () -> Unit,

    onLeaveClick : () -> Unit,

    onNotificationClick: () -> Unit,
    onLogout : () -> Unit,
    onAttendanceHistoryClick : () -> Unit,
    viewModel : ProfileViewModel

) {


    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    var showEditSheet by remember { mutableStateOf(false) }
    Scaffold(

        containerColor = Color(0xFFF8FAFC)

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 20.dp)

        ) {

            Spacer(modifier = Modifier.height(16.dp))

            ProfileTopBar(

                employeeName = uiState.employeeName,

                onBackClick = onBackClick,

                onNotificationClick = onNotificationClick

            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileHeaderCard(
                uiState = uiState,
                onEditProfileClick = {
                    showEditSheet = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            MenuSection(

                onAttendanceClick = onAttendanceHistoryClick,

                onSettingsClick = { },

                onLeaveClick = onLeaveClick,

                onLogoutClick = {
                    showLogoutDialog = true
                }

            )

            Spacer(modifier = Modifier.height(20.dp))

            AboutCard(
                appVersion = "1.0.0",
                companyName = "Datamann",
                appName = "WorkPulse"
            )

            Spacer(modifier = Modifier.height(24.dp))

        }

    }

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {
                showLogoutDialog = false
            },

            title = {
                Text("Logout")
            },

            text = {
                Text("Are you sure you want to logout from WorkPulse?")
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        showLogoutDialog = false

                        onLogout()

                    }

                ) {

                    Text(
                        text = "Logout",
                        color = Color.Red
                    )

                }

            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showLogoutDialog = false

                    }

                ) {

                    Text("Cancel")

                }

            }

        )

    }


    if (showEditSheet) {

        EditProfileBottomSheet(

            profile = uiState,

            onDismiss = {

                showEditSheet = false

            },

            onSave = { email, imagePath ->

                viewModel.updateProfile(
                    personalEmail = email,
                    imageUri = imagePath.orEmpty()
                )

                showEditSheet = false

            }

        )

    }


}

@Composable
private fun ProfileTopBar(

    employeeName: String,

    onBackClick: () -> Unit,

    onNotificationClick: () -> Unit

) {

    Column {

        Row(

            modifier = Modifier.fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            // Back Button
            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF0F172A)
                )

            }

            // Logo
            Image(

                painter = painterResource(R.drawable.workpulse_logo),

                contentDescription = "WorkPulse",

                modifier = Modifier.size(64.dp)

            )

            // Notification
            BadgedBox(

                badge = {

                    Badge(
                        containerColor = Color(0xFF22C55E)
                    )

                }

            ) {

                IconButton(
                    onClick = onNotificationClick
                ) {

                    Icon(

                        imageVector = Icons.Outlined.Notifications,

                        contentDescription = "Notifications",

                        tint = Color(0xFF0F172A)

                    )

                }

            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(

            text = "Profile",

            style = MaterialTheme.typography.headlineLarge,

            fontWeight = FontWeight.Bold,

            color = Color(0xFF0F172A)

        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(

            text = "Welcome back, $employeeName",

            style = MaterialTheme.typography.bodyLarge,

            color = Color.Gray

        )

    }

}



@Composable
private fun ProfileHeaderCard(

    uiState: ProfileUiState,

    onEditProfileClick: () -> Unit

) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(24.dp),

        elevation = CardDefaults.cardElevation(6.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {

        Column {

            Box {

                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFFF3F8FF),
                                    Color(0xFFEAFBF5)
                                )
                            )
                        )
                        .padding(20.dp)

                ) {

                    Row(

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Card(

                            modifier = Modifier.size(110.dp),

                            shape = CircleShape,

                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )

                        ) {

                            if (uiState.employeeImage.isNotBlank()) {

                                AsyncImage(
                                    model = File(uiState.employeeImage),
                                    contentDescription = "Profile",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                            } else {

                                Image(
                                    painter = painterResource(R.drawable.profile_placeholder),
                                    contentDescription = "Profile",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                            }

                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(

                                text = uiState.employeeName,

                                style = MaterialTheme.typography.headlineSmall,

                                fontWeight = FontWeight.Bold

                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(

                                text = uiState.designation,

                                style = MaterialTheme.typography.titleMedium,

                                color = Color.Gray

                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(

                                text = uiState.companyEmail,

                                style = MaterialTheme.typography.bodyMedium,

                                color = Color.Gray

                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(

                                color = Color(0xFFE8F8EE),

                                shape = RoundedCornerShape(50)

                            ) {

                                Row(

                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),

                                    verticalAlignment = Alignment.CenterVertically

                                ) {

                                    Box(

                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                Color(0xFF22C55E),
                                                CircleShape
                                            )

                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(

                                        text = "Active",

                                        color = Color(0xFF22C55E),

                                        fontWeight = FontWeight.Medium

                                    )

                                }

                            }

                        }

                    }

                }

                FloatingActionButton(

                    onClick = onEditProfileClick,

                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),

                    containerColor = Color.White,

                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 4.dp
                    )

                ) {

                    Icon(

                        imageVector = Icons.Default.Edit,

                        contentDescription = null,

                        tint = Color(0xFF2563EB)

                    )

                }

            }

            HorizontalDivider()

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),

                horizontalArrangement = Arrangement.SpaceEvenly

            ) {

                QuickInfoItem(

                    title = "Employee ID",

                    value = uiState.employeeId,

                    icon = Icons.Outlined.Badge,

                    iconColor = Color(0xFF22C55E)

                )

                QuickInfoItem(

                    title = "Department",

                    value = uiState.department,

                    icon = Icons.Outlined.BusinessCenter,

                    iconColor = Color(0xFF2563EB)

                )

                QuickInfoItem(

                    title = "Location",

                    value = uiState.currentAddress,

                    icon = Icons.Outlined.LocationOn,

                    iconColor = Color(0xFFFF9800)

                )

            }

        }

    }

}


@Composable
private fun QuickInfoItem(

    title: String,

    value: String,

    icon: ImageVector,

    iconColor: Color

) {

    Column(

        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier.width(100.dp)

    ) {

        Icon(

            imageVector = icon,

            contentDescription = null,

            tint = iconColor,

            modifier = Modifier.size(28.dp)

        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(

            text = title,

            style = MaterialTheme.typography.bodySmall,

            color = Color.Gray

        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(

            text = value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,

            style = MaterialTheme.typography.bodySmall,

            fontWeight = FontWeight.Bold

        )

    }

}