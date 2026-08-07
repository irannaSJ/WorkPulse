package com.example.workpulse.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPulseTopBar(

    title: String,

    onBackClick: () -> Unit

) {

    CenterAlignedTopAppBar(

        title = {

            Text(

                text = title,

                style = MaterialTheme.typography.titleLarge

            )

        },

        navigationIcon = {

            IconButton(

                onClick = onBackClick

            ) {

                Icon(

                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,

                    contentDescription = "Back"

                )

            }

        }

    )

}