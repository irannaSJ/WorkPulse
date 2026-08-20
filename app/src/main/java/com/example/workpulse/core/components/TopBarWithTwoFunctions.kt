package com.example.workpulse.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Pages
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class TopBarWithTwoFunctions {
}

@Composable
fun TopBarWithTwoFunctions(

    onBackClick: () -> Unit,

    onButtonClick: () -> Unit

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
                text = "Compensatory Off Application",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface

            )


            IconButton(
                onClick = onButtonClick
            ) {

                Icon(

                    imageVector = Icons.Outlined.Pages,

                    contentDescription = "Comp-Off Applications History",

                    tint = MaterialTheme.colorScheme.onSurface

                )

            }

//            }

        }

    }

}