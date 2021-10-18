package com.alurwa.berkelas.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alurwa.common.model.RoomData
import com.alurwa.common.model.User
import com.alurwa.data.repository.auth.AuthRepository
import com.alurwa.data.repository.maincard.MainCardRepository
import com.alurwa.data.repository.room.RoomRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository,
    private val homeCardRepository: MainCardRepository
) : ViewModel() {
    val isLogged = authRepository.isLogged()

    val observeUser = userRepository
        .observeUser()
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)

    val observeRoom = roomRepository.observeRoom().shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        replay = 1
    )

    val observeHomeCard = homeCardRepository.observeMainCardList()

    val isLoggedListener = authRepository.isLoggedListener()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _room = MutableStateFlow<RoomData>(RoomData.EMPTY)
    val room = _room.asStateFlow()

    private val _destinationId = MutableStateFlow(-1)
    val destinationId = _destinationId.asStateFlow()

    fun setUser(user: User) {
        _user.value = user
    }

    fun setRoom(roomData: RoomData) {
        _room.value = roomData
    }

    fun setDestinationId(value: Int) {
        _destinationId.value = value
    }

    fun signOut() {
        authRepository.signOut()
    }
}