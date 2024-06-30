package com.example.localstorage

import com.example.localstorage.room.Contact

data class ContactState(
    val contacts: List<Contact> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val sortType: SortType = SortType.FIRST_NAME,
    val isAddingContactMode: Boolean = false
)