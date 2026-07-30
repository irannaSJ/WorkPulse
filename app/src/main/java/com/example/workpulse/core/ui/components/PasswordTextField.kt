package com.example.workpulse.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordTextField(

    value: String,

    onValueChange: (String) -> Unit,

    modifier: Modifier = Modifier,

    label: String = "Password",

    placeholder: String = "Enter your password",

    isError: Boolean = false,

    supportingText: String? = null,

    enabled: Boolean = true

) {

    var passwordVisible by rememberSaveable {

        mutableStateOf(false)

    }

    OutlinedTextField(

        value = value,

        onValueChange = onValueChange,

        modifier = modifier.fillMaxWidth(),

        singleLine = true,

        enabled = enabled,

        isError = isError,

        label = {

            Text(label)

        },

        placeholder = {

            Text(placeholder)

        },

        leadingIcon = {

            Icon(

                imageVector = Icons.Outlined.Lock,

                contentDescription = null

            )

        },

        trailingIcon = {

            IconButton(

                onClick = {

                    passwordVisible = !passwordVisible

                }

            ) {

                Icon(

                    imageVector = if (passwordVisible)
                        Icons.Outlined.VisibilityOff
                    else
                        Icons.Outlined.Visibility,

                    contentDescription = if (passwordVisible)
                        "Hide Password"
                    else
                        "Show Password"

                )

            }

        },

        visualTransformation =

            if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),

        keyboardOptions = KeyboardOptions(

            imeAction = ImeAction.Done

        ),

        supportingText = {

            supportingText?.let {

                Text(it)

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