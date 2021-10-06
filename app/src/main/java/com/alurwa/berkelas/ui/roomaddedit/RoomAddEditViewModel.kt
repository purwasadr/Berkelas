package com.alurwa.berkelas.ui.roomaddedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.model.RoomData
import com.alurwa.data.model.RoomAddParams
import com.alurwa.data.repository.room.RoomRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomAddEditViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val mode = stateHandle.get<Int>(RoomAddEditActivity.EXTRA_MODE)!!

    val room = if (mode == RoomAddEditActivity.MODE_EDIT) {
        stateHandle.get<RoomData>(RoomAddEditActivity.EXTRA_ROOM)!!
    } else {
        null
    }

    fun addRoom(roomData: RoomAddParams) = roomRepository.addRoomData(roomData)

    fun editRoom(roomData: RoomData) = roomRepository.ediRoomData(roomData)
}