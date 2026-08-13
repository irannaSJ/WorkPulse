package com.example.workpulse.feature.compOffApplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.remote.creation.dsl.fillMaxSize
import androidx.compose.remote.creation.dsl.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.components.WorkPulseTopBar
import com.example.workpulse.feature.compOffApplication.components.CompOffRequestCard


@Composable
fun CompOffApplicationScreen(
    uiState: CompOffApplicationUiState,
    onBackClick : () -> Unit,
    onFromDateClick: () -> Unit,
    onToDateClick : () -> Unit,
    onReasonChanged : (String) -> Unit,
    onSubmitClick:() -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            WorkPulseTopBar(
                title = "Compensatory Off Request",
                onBackClick = onBackClick
            )
        }

    ) {
        paddingValues ->
        CompOffApplicationContent(
            paddingValues = paddingValues,
            uiState = uiState,
            onFromDateClick = onFromDateClick,
            onToDateClick = onToDateClick,
            onReasonChanged = onReasonChanged,
            onSubmitClick = onSubmitClick
        )

    }
}


@Composable
private fun CompOffApplicationContent(
    paddingValues: PaddingValues,
    uiState: CompOffApplicationUiState,
    onFromDateClick: () -> Unit,
    onToDateClick: () -> Unit,
    onReasonChanged: (String) -> Unit,
    onSubmitClick: () -> Unit
){
    if(uiState.isLoading){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        CompOffRequestCard(
            uiState =uiState,
            onFromDateClick = onFromDateClick,
            onToDateClick = onToDateClick,
            onReasonChanged = onReasonChanged,
        )

        Button(
            onClick = onSubmitClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSubmitting
        ){
            if(uiState.isSubmitting){
                CircularProgressIndicator()
            }else{
                Text(
                    text= "Submit Request",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun previewCompOffApplication(){
//    CompOffApplicationScreen(
//        uiState = CompOffApplicationUiState(),
//        onBackClick = {},
//        onFromDateClick = {},
//        onToDateClick = {},
//        onReasonChanged = {},
//        onSubmitClick = {}
//    )
//}