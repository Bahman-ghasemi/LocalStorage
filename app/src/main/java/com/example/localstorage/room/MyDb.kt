package com.example.localstorage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Person::class],
    version = 1
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class MyDb : RoomDatabase() {
    abstract val dao: PersonDao
}