package com.example.localstorage

enum class SortType {
    FIRST_NAME,
    LAST_NAME,
    PHONE_NUMBER;

    override fun toString(): String {
        return name.replace("_", " ")
    }
}