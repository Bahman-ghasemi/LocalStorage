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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.localstorage.room.Contact
import com.example.localstorage.room.MyDb
import com.example.localstorage.room.Number
import com.example.localstorage.room.NumberType
import com.example.localstorage.ui.theme.LocalStorageTheme
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            MyDb::class.java,
            "transactions.db"
        ).build()
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
                        val _contact = db.contactDao.getContacts()
                        val _number = db.numberDao.getNumbers()
                        val state = _contact.combine(_number) { c, n ->
                            c.map {
                                it.copy(numbers = n)
                            }
                        }.collectAsState(initial = emptyList()).value

                        SideEffect {
                            state.forEach(::println)
                        }

                        Button(onClick = {
                            scope.launch {
                                val contact = contacts[Random.nextInt(0, contacts.lastIndex)]
                                val numbers = numbers()
                                db.contactDao.insertContactWithNumbers(contact, numbers)
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

val contacts = listOf(
    Contact("Bahman", "Ghasemi"),
    Contact("Ali", "Zare"),
    Contact("Kamran", "Palang"),
    Contact("Reza", "Dx")
)


fun numbers(): List<Number> {
    val list = mutableListOf<Number>()
    repeat((1..5).count()) {
        list.add(
            Number(
                type = NumberType.MOBILE,
                number = "09" + (Random.nextLong(100000000, 929999999)).toString()
            )
        )
    }
    return list
}