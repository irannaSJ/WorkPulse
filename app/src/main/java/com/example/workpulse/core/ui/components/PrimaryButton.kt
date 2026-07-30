package com.example.workpulse.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.theme.AppGradients

@Composable
fun PrimaryButton(

    text: String,

    onClick: () -> Unit,

    modifier: Modifier = Modifier,

    enabled: Boolean = true,

    loading: Boolean = false

) {

    Button(

        onClick = onClick,

        enabled = enabled && !loading,

        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),

        shape = RoundedCornerShape(16.dp),

        colors = ButtonDefaults.buttonColors(

            containerColor = Color.Transparent,

            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,

            contentColor = Color.White

        ),

        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)

    ) {

        Box(

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = if (enabled)
                        AppGradients.Primary
                    else
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        ),
                    shape = RoundedCornerShape(16.dp)
                ),

            contentAlignment = Alignment.Center

        ) {

            if (loading) {

                CircularProgressIndicator(

                    modifier = Modifier.padding(2.dp),

                    strokeWidth = 2.dp,

                    color = Color.White

                )

            } else {

                Text(

                    text = text,

                    style = MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.SemiBold,

                    color = Color.White

                )

            }

        }

    }

}