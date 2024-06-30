package com.example.localstorage

import com.example.localstorage.room.Contact

sealed interface ContactEvent {
    data class setFirstName(val firstName: String) : ContactEvent
    data class setLastName(val lastName: String) : ContactEvent
    data class setPhoneNumber(val phoneNumber: String) : ContactEvent
    object saveContact : ContactEvent
    data class sortList(val sortType: SortType) : ContactEvent
    data class deleteContact(val contact: Contact) : ContactEvent
    object showDialog : ContactEvent
    object hideDialog : ContactEvent

}

