package com.example.localstorage.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Upsert
    suspend fun upsertPerson(person: Person)

    @Query("SELECT * FROM Person")
    fun getPersons(): Flow<List<Person>>
}