package com.example.localstorage.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Contact(
    val firstName: String,
    val lastName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Ignore
    val numbers: List<Number> = emptyList()
) {
    constructor(firstName: String, lastName: String, id: Int)
            : this(firstName, lastName, id, emptyList())
}
