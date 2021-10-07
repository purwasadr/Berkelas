package com.alurwa.berkelas.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alurwa.common.model.RoomData
import com.alurwa.common.model.User
import com.alurwa.data.repository.auth.AuthRepository
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
    private val roomRepository: RoomRepository
) : ViewModel() {
    val isLogged = authRepository.isLogged()

    val observeUser = userRepository
        .observeUser()
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    val observeRoom = roomRepository.observeRoom().shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        replay = 1
    )

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _room = MutableStateFlow<RoomData>(RoomData.EMPTY)
    val room = _room.asStateFlow()

    fun setUser(user: User) {
        _user.value = user
    }

    fun setRoom(roomData: RoomData) {
        _room.value = roomData
    }

    fun signOut() {
        authRepository.signOut()
    }
}