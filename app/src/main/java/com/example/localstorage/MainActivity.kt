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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.localstorage.room.Address
import com.example.localstorage.room.MyDb
import com.example.localstorage.room.Person
import com.example.localstorage.ui.theme.LocalStorageTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            MyDb::class.java,
            "person.db"
        )
            .createFromAsset("database/pre_db.db")
            .build()
    }

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
                        verticalArrangement = Arrangement.Center
                    ) {
                        val scope = rememberCoroutineScope()

                        SideEffect {
                            scope.launch {
                                db.dao.getPersons().collect {
                                    it.onEach(::println)
                                }
                            }
                        }

                        Button(onClick = {
                            scope.launch {
                                val address = Address("Khosh", 4)
                                val person = Person("bahman", "ghasemi", 33, address)
                                db.dao.upsertPerson(person)
                            }
                        }) {
                            Text(text = "Add")
                        }
                    }
                }
            }
        }
    }
}