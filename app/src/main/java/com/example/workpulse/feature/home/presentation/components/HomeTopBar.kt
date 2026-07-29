package com.example.workpulse.feature.home.presentation.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.workpulse.R
import com.example.workpulse.feature.home.presentation.HomeUiState
import java.io.File
import java.util.Calendar

@Composable
fun HomeTopBar(

    uiState: HomeUiState,

    onProfileClick: () -> Unit

) {

    val greeting = remember {

        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {

            in 0..11 -> "Good Morning"

            in 12..16 -> "Good Afternoon"

            else -> "Good Evening"

        }

    }

    Row(

        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceBetween,

        verticalAlignment = Alignment.Top

    ) {

        Column {

            Image(

                painter = painterResource(R.drawable.workpulse_logo),

                contentDescription = "Logo",

                modifier = Modifier.height(70.dp)

            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(

                text = "$greeting",

                fontSize = 32.sp,

                fontWeight = FontWeight.Bold,

                color = Color(0xFF111827)

            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(

                text = "Let's make today productive!",

                fontSize = 18.sp,

                color = Color(0xFF6B7280)

            )

        }

        ProfileAvatar(

            employeeName = uiState.employeeName,
            uiState = uiState,

            onClick = onProfileClick

        )

    }

}

@Composable
private fun ProfileAvatar(

    employeeName: String,
    uiState: HomeUiState,

    onClick: () -> Unit

) {

    Box {

        Card(

            modifier = Modifier.size(82.dp),

            shape = CircleShape,

            elevation = CardDefaults.cardElevation(8.dp),

            onClick = onClick

        ) {

            if (uiState.profileImage.isNotBlank()) {

                AsyncImage(
                    model = File(uiState.profileImage),
                    contentDescription = employeeName,
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

            } else {

                Image(
                    painter = painterResource(R.drawable.profile_placeholder),
                    contentDescription = employeeName,
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

            }

        }

        Box(

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-2).dp, (-2).dp)
                .size(18.dp)
                .background(
                    Color(0xFF22C55E),
                    CircleShape
                )
                .border(
                    3.dp,
                    Color.White,
                    CircleShape
                )

        )

    }

}