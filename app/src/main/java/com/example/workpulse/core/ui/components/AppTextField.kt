package com.example.workpulse.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(

    value: String,

    onValueChange: (String) -> Unit,

    label: String,

    modifier: Modifier = Modifier,

    placeholder: String = "",

    leadingIcon: ImageVector? = null,

    isError: Boolean = false,

    supportingText: String? = null,

    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,

    enabled: Boolean = true,

    readOnly: Boolean = false,

    singleLine: Boolean = true

) {

    OutlinedTextField(

        value = value,

        onValueChange = onValueChange,

        modifier = modifier.fillMaxWidth(),

        enabled = enabled,

        readOnly = readOnly,

        singleLine = singleLine,

        isError = isError,

        keyboardOptions = keyboardOptions,

        label = {

            Text(label)

        },

        placeholder = {

            if (placeholder.isNotEmpty()) {

                Text(placeholder)

            }

        },

        leadingIcon = {

            leadingIcon?.let {

                Icon(

                    imageVector = it,

                    contentDescription = null

                )

            }

        },

        supportingText = {

            if (supportingText != null) {

                Text(supportingText)

            }

        },

        shape = RoundedCornerShape(16.dp),

        colors = OutlinedTextFieldDefaults.colors(

            focusedBorderColor = MaterialTheme.colorScheme.primary,

            unfocusedBorderColor = MaterialTheme.colorScheme.outline,

            focusedLabelColor = MaterialTheme.colorScheme.primary,

            cursorColor = MaterialTheme.colorScheme.primary,

            errorBorderColor = MaterialTheme.colorScheme.error,

            errorLabelColor = MaterialTheme.colorScheme.error

        )

    )

}