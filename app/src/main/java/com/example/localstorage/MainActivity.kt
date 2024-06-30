package com.example.localstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.localstorage.ui.theme.LocalStorageTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val dataStore by preferencesDataStore("MyDataStore")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalStorageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            12.dp,
                            alignment = Alignment.CenterVertically
                        )
                    ) {
                        val scope = rememberCoroutineScope()
                        var name by remember { mutableStateOf("") }
                        var age by remember { mutableStateOf("") }

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(text = "Name") })

                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text(text = "Age") })

                        Button(onClick = {
                            scope.launch {
                                dataStore.edit {
                                    it[stringPreferencesKey("name")] = name
                                    it[intPreferencesKey("age")] = age.toIntOrNull() ?: -1
                                }
                            }
                        }) {
                            Text(text = "Save")
                        }

                        OutlinedButton(onClick = {
                            scope.launch {
                                dataStore.data.let {
                                    name = it.first()[stringPreferencesKey("name")] ?: ""
                                    age = (it.first()[intPreferencesKey("age")] ?: "").toString()
                                }
                            }
                        }) {
                            Text(text = "Load")
                        }
                    }
                }
            }
        }
    }
}
