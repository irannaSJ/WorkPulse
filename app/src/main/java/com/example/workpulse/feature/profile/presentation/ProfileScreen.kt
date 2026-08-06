package com.example.workpulse.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
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
import androidx.compose.remote.creation.dsl.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.R
import com.example.workpulse.feature.profile.presentation.components.AboutCard
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.workpulse.feature.home.presentation.components.drawerRelated.LogoutDialog
//import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workpulse.feature.profile.presentation.components.EditProfileBottomSheet
import com.example.workpulse.feature.profile.presentation.components.EmploymentInformationCard
import com.example.workpulse.feature.profile.presentation.components.LogoutCard
import com.example.workpulse.feature.profile.presentation.components.PersonalInformationCard
import com.example.workpulse.feature.profile.presentation.components.ProfileHeader
import java.io.File

@Composable
fun ProfileScreen(

    uiState: ProfileUiState,

    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onLogoutClick:() -> Unit,

    viewModel : ProfileViewModel,
    modifier : Modifier = Modifier

) {


    var showEditSheet by remember { mutableStateOf(false) }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }



        LazyColumn(

            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

//            Spacer(modifier = Modifier.height(16.dp))

            item {

                ProfileTopBar(

                    onBackClick = onBackClick,

                    onEditClick = {
//                        onEditClick
                        showEditSheet = true
                    }

                )
            }

            item {
                ProfileHeader(
//                    employeeName = uiState.employeeName,
//                    designation = uiState.designation,
//                    employeeId = uiState.employeeId,
                    uiState = uiState
                )

            }


            item{
                EmploymentInformationCard(
                    employeeId = uiState.employeeId,
                    company = uiState.company,
                    department = uiState.department,
                    joiningDate = uiState.dateOfJoining,
                    designation = uiState.designation
                )
            }



            item{
                PersonalInformationCard(
                    companyEmail = uiState.companyEmail,
                    personalEmail = uiState.personalEmail,
                    address = uiState.currentAddress,
                    mobileNumber = uiState.mobileNumber,

                )
            }

            item{
                AboutCard(
                    version = "1.0.0v",
                    buildNumber = "100",
                    onPrivacyPolicyClick = {},
                    onTermsClick = {},
                )
            }

            item{
                LogoutCard(
                    onLogoutClick = {
                        showLogoutDialog = true
                    }
                )
            }


        }

    if (showLogoutDialog){
        LogoutDialog(
            onDismiss = {
                showLogoutDialog = false
            },
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
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

    onBackClick: () -> Unit,

    onEditClick: () -> Unit

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
                    tint = MaterialTheme.colorScheme.onSurface
                )

            }

            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface

            )


                IconButton(
                    onClick = onEditClick
                ) {

                    Icon(

                        imageVector = Icons.Outlined.Edit,

                        contentDescription = "Notifications",

                        tint = Color(0xFF0F172A)

                    )

                }

//            }

        }

    }

}
