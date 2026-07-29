package com.example.workpulse.feature.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.profile.ProfileUiState


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.core.util.ImageStorageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBottomSheet(

    profile: ProfileUiState,

    onDismiss: () -> Unit,

    onSave: (String, String?) -> Unit

) {

    val context = LocalContext.current

    var personalEmail by remember(profile.personalEmail) {
        mutableStateOf(profile.personalEmail)
    }

    var selectedImageUri by remember(profile.imageUri) {
        mutableStateOf(
            profile.imageUri?.takeIf { it.isNotBlank() }?.let(Uri::parse)
        )
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->

        if (uri != null) {
            selectedImageUri = uri
        }

    }

    ModalBottomSheet(

        onDismissRequest = onDismiss

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())

        ) {

            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                ) {
                    Text("Change Photo")
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(Modifier.height(24.dp))

            ReadOnlyField(
                label = "App Uid",
                value =profile.app_uid
            )

            ReadOnlyField(
                label = "Employee Name",
                value = profile.employeeName
            )

            ReadOnlyField(
                label = "Employee ID",
                value = profile.employeeId
            )

            ReadOnlyField(
                label = "Company Email",
                value = profile.companyEmail
            )

            EditableField(
                label = "Personal Email",
                value = personalEmail,
                onValueChange = {
                    personalEmail = it
                }
            )

            ReadOnlyField(
                label = "Department",
                value = profile.department
            )

            ReadOnlyField(
                label = "Designation",
                value = profile.designation
            )
//
//            ReadOnlyField(
//                label = "Phone Number",
//                value = profile.phoneNumber
//            )

            Spacer(Modifier.height(24.dp))

            val imagePath =
                selectedImageUri?.let {
                    ImageStorageManager.saveProfileImage(
                        context,
                        it,
                        profile.employeeId
                    )
                }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    val imagePath = selectedImageUri?.let {
                        ImageStorageManager.saveProfileImage(
                            context = context,
                            uri = it,
                            employeeId = profile.employeeId
                        )
                    }

                    onSave(
                        personalEmail,
                        imagePath
                    )

                }
            ) {
                Text("Save Changes")
            }

            Spacer(Modifier.height(20.dp))

        }

    }

}


@Composable
private fun ReadOnlyField(

    label: String,

    value: String

) {

    OutlinedTextField(

        value = value,

        onValueChange = {},

        readOnly = true,

        enabled = false,

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),

        label = {

            Text(label)

        }

    )

}


@Composable
private fun EditableField(

    label: String,

    value: String,

    onValueChange: (String) -> Unit

) {

    OutlinedTextField(

        value = value,

        onValueChange = onValueChange,

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),

        label = {

            Text(label)

        },

        singleLine = true

    )

}