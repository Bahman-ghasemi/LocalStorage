package com.example.localstorage.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Contact::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Number(
    val type: NumberType,
    val number: String,
    val contactId: Long = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

enum class NumberType {
    MOBILE,
    HOME,
    WORK,
    OTHER
}
