package com.example.localstorage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddContactScreen(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEvent(ContactEvent.hideDialog) },
        title = { Text(text = "Add Contact") },
        text = {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = { onEvent(ContactEvent.setFirstName(it)) },
                    label = { Text(text = "First Name") }
                )

                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = { onEvent(ContactEvent.setLastName(it)) },
                    label = { Text(text = "Last Name") }
                )

                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { onEvent(ContactEvent.setPhoneNumber(it)) },
                    label = { Text(text = "Phone Number") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(ContactEvent.saveContact)
            }) {
                Text(text = "Save")
            }
        }
    )
}