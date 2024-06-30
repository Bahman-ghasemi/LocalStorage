package com.example.localstorage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localstorage.room.Contact
import com.example.localstorage.room.ContactDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val dao: ContactDao
) : ViewModel() {

    private val _sortType = MutableStateFlow<SortType>(SortType.FIRST_NAME)
    private val _contacts = _sortType.flatMapLatest {
        when (it) {
            SortType.FIRST_NAME -> dao.getContactsSortedByFirstNameASC()
            SortType.LAST_NAME -> dao.getContactsSortedByLastNameASC()
            SortType.PHONE_NUMBER -> dao.getContactsSortedByPhoneNumberASC()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow<ContactState>(ContactState())
    val state = combine(_sortType, _contacts, _state) { sort, contactList, contactState ->
        contactState.copy(
            contacts = contactList,
            sortType = sort
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.setFirstName -> {
                _state.update {
                    it.copy(firstName = event.firstName)
                }
            }

            is ContactEvent.setLastName -> {
                _state.update {
                    it.copy(lastName = event.lastName)
                }
            }

            is ContactEvent.setPhoneNumber -> {
                _state.update {
                    it.copy(phoneNumber = event.phoneNumber)
                }
            }

            ContactEvent.showDialog -> {
                _state.update {
                    it.copy(isAddingContactMode = true)
                }
            }

            ContactEvent.hideDialog -> {
                _state.update {
                    it.copy(isAddingContactMode = false)
                }
            }

            is ContactEvent.saveContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty())
                    return

                val contact = Contact(firstName, lastName, phoneNumber)
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                _state.update {
                    it.copy(
                        isAddingContactMode = false,
                        firstName = "",
                        lastName = "",
                        phoneNumber = ""
                    )
                }
            }

            is ContactEvent.deleteContact -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }
            }

            is ContactEvent.sortList -> {
                _sortType.value = event.sortType
            }
        }
    }
}