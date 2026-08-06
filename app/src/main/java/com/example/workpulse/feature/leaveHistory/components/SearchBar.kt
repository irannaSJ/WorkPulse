package com.example.workpulse.feature.leaveHistory.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.theme.WorkPulseTheme


@Composable
fun SearchBar(

    query: String,

    onQueryChange: (String) -> Unit,

    modifier: Modifier = Modifier

) {

    OutlinedTextField(

        value = query,

        onValueChange = onQueryChange,

        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),

        singleLine = true,

        placeholder = {

            Text(
                text = "Search leave type or reason..."
            )

        },

        leadingIcon = {

            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )

        },

        trailingIcon = {

            if (query.isNotEmpty()) {

                IconButton(

                    onClick = {

                        onQueryChange("")

                    }

                ) {

                    Icon(

                        imageVector = Icons.Outlined.Close,

                        contentDescription = "Clear Search"

                    )

                }

            }

        },

        shape = MaterialTheme.shapes.large

    )

}


@Preview(showBackground = true)
@Composable
private fun LeaveHistorySearchBarPreview() {

    WorkPulseTheme {

        SearchBar(

            query = "",

            onQueryChange = {}

        )

    }

}