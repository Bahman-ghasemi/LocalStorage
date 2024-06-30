package com.example.localstorage.di

import android.content.Context
import androidx.room.Room
import com.example.localstorage.room.ContactDao
import com.example.localstorage.room.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRoomDb(@ApplicationContext context: Context): ContactDatabase {
        return Room.databaseBuilder(
            context,
            ContactDatabase::class.java,
            "Contacts.db"
        ).build()
    }

    @Singleton
    @Provides
    fun providesContactDao(db: ContactDatabase): ContactDao = db.dao
}