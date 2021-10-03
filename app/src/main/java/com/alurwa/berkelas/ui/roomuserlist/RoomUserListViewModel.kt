package com.alurwa.berkelas.ui.roomuserlist

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomUserListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val users = userRepository.observeUsersByRoomId()
}