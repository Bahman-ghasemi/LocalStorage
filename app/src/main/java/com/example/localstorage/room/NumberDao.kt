package com.example.localstorage.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NumberDao {

    @Upsert
    suspend fun upsertNumbers(numbers: List<Number>)

    @Query("SELECT * FROM Number")
    fun getNumbers(): Flow<List<Number>>
}