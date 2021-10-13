package com.alurwa.berkelas.ui.roomlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.room.RoomRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val rooms = roomRepository.observeRooms()

    fun getMyRoomId() = userRepository.getRoomIdLocal()
}