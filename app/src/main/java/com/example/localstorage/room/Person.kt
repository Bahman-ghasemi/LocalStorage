package com.example.localstorage.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Person(
    val firstName: String,
    val lastName: String,
    val age: Int,
    @Embedded
    val address: Address,
    val createdDate : LocalDate = LocalDate.now(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

class LocalDateTimeConverter{

    @TypeConverter
    fun toString(localDate: LocalDate):String{
        return localDate.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateTime: String):LocalDate{
        return LocalDate.parse(dateTime)
    }
}