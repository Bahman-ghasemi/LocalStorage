package com.example.localstorage.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_users")
data class User(
    val userName: String,
    @PrimaryKey(autoGenerate = false)
    val email: String,
    @ColumnInfo(name = "dateAdded", defaultValue = "0")
    val dateAdded: Long
)