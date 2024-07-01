package com.example.localstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.localstorage.room.MyDb
import com.example.localstorage.room.School
import com.example.localstorage.room.User
import com.example.localstorage.ui.theme.LocalStorageTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            MyDb::class.java,
            "Users.db"
        )
            .addMigrations(MyDb.Companion.migtation4to5)
            .build()

        enableEdgeToEdge()
        setContent {
            LocalStorageTheme {
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    scope.launch(Dispatchers.IO) {
                        db.dao.getSchools().forEach(::println)
                    }

                    /*(1..10).forEach {
                        db.dao.upsertSchool(
                            School(
                                name = "School #$it"
                            )
                        )
                    }*/
                }
            }
        }
    }
}