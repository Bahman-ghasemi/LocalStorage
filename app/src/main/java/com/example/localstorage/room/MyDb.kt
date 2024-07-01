package com.example.localstorage.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Contact::class, Number::class],
    version = 1
)
abstract class MyDb : RoomDatabase() {
    abstract val contactDao: ContactDao
    abstract val numberDao: NumberDao
}