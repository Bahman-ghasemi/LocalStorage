package com.example.localstorage.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM Contact ORDER BY firstName ASC")
    fun getContactsSortedByFirstNameASC(): Flow<List<Contact>>

    @Query("SELECT * FROM Contact ORDER BY lastName ASC")
    fun getContactsSortedByLastNameASC(): Flow<List<Contact>>

    @Query("SELECT * FROM Contact ORDER BY phoneNumber ASC")
    fun getContactsSortedByPhoneNumberASC(): Flow<List<Contact>>
}