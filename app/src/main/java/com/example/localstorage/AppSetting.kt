package com.example.localstorage

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class AppSetting(
    val language: Language = Language.English,
    val userLocation: PersistentList<Location> = persistentListOf()
)

enum class Language {
    English, Farsi
}

@Serializable
data class Location(
    val lat: Double,
    val long: Double
)