package com.alurwa.berkelas.ui.roomaddedit

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.room.RoomRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class RoomAddEditViewModel constructor(
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

}