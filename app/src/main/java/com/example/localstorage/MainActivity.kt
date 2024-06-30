package com.example.localstorage

import android.content.Context
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.localstorage.ui.theme.LocalStorageTheme

class MainActivity : ComponentActivity() {
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
                        val prefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
                        var name by remember { mutableStateOf("") }
                        var age by remember { mutableStateOf("") }

                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(text = "Name")})
                        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text(text = "Age")})
                        Button(onClick = {
                            prefs.edit()
                                .putString("name_key", name)
                                .putInt("age_key", age.toIntOrNull() ?: -1)
                                .apply()
                        }) {
                            Text(text = "Save")
                        }

                        OutlinedButton(onClick = {
                            name = prefs.getString("name_key", "") ?: ""
                            age = prefs.getInt("age_key", -1).toString()

                        }) {
                            Text(text = "Load")
                        }
                    }
                }
            }
        }
    }
}