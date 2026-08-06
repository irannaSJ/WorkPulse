package com.example.workpulse.feature.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileRoute(
    onBackClick: () -> Unit,
    onLogoutSuccess : () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),


) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.isLogoutSuccessful) {
        if(uiState.isLogoutSuccessful){
            onLogoutSuccess()
        }
    }


    ProfileScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onEditClick = {
            viewModel.enableEditing()
        },
        onLogoutClick = { viewModel.logout() },
        viewModel = viewModel
    )
}