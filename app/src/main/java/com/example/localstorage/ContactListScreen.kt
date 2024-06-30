package com.example.localstorage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactListScreen(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit
) {
    Scaffold(Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(onClick = {
            onEvent(ContactEvent.showDialog)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Contact")
        }
    },
        topBar = {
            LazyRow(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(SortType.entries) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            onEvent(ContactEvent.sortList(it))
                        }) {
                        RadioButton(
                            selected = (it == state.sortType),
                            onClick = { onEvent(ContactEvent.sortList(it)) }
                        )
                        Text(text = it.toString())
                    }
                }
            }
        }
    )
    { paddingValues ->

        if (state.isAddingContactMode)
            AddContactScreen(state, onEvent)

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        )
        {
            items(state.contacts) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "${it.firstName} ${it.lastName}", fontSize = 20.sp)
                        Text(text = it.phoneNumber, fontSize = 12.sp)
                    }
                    IconButton(onClick = { onEvent(ContactEvent.deleteContact(it)) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Contact"
                        )
                    }
                }
            }
        }
    }
}