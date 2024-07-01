package com.example.localstorage.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM tbl_users")
    fun getUsers():List<User>

    @Upsert
    suspend fun upsertSchool(school: School)

    @Query("SELECT * FROM School")
    suspend fun getSchools():List<School>
}