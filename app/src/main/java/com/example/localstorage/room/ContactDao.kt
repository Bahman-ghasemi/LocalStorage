package com.example.localstorage.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsertContact(contact: Contact): Long

    @Query("SELECT * FROM Contact")
    fun getContacts(): Flow<List<Contact>>

    @Upsert
    suspend fun upsertNumbers(numbers:List<Number>)

    @Transaction
    suspend fun insertContactWithNumbers(contact: Contact, numbers: List<Number>) {
        val contactId = upsertContact(contact)
        val numbersWithContactId = numbers.map { it.copy(contactId = contactId) }
        upsertNumbers(numbersWithContactId)
    }
}