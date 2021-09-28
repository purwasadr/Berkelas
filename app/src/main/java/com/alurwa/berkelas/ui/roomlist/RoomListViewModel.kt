package com.alurwa.berkelas.ui.roomlist

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.room.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    val rooms = roomRepository.getRooms()
}