package com.alurwa.berkelas.ui.roomdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.model.RoomData
import com.alurwa.common.model.User
import com.alurwa.data.model.RoomSetParams
import com.alurwa.data.repository.room.RoomRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val roomExtra = stateHandle.get<RoomData>(RoomDetailActivity.EXTRA_ROOM)!!

    val observeRoom = roomRepository.observeRoom(roomExtra.id)

    val userOther = userRepository.getUser(roomExtra.creatorId)

    val observeUser = userRepository.observeUser()

    private val _creatorName = MutableStateFlow<String>("")
    val creatorName = _creatorName.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _room = MutableStateFlow<RoomData?>(null)
    val room = _room.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun setCreatorName(name: String) {
        _creatorName.value = name
    }

    fun setUser(user: User) {
        _user.value = user
    }

    fun setRoom(roomData: RoomData) {
        _room.value = roomData
    }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun applyRoom(roomSetParams: RoomSetParams) = userRepository.setRoom(roomSetParams)

    fun removeRoom() = userRepository.setRoom(
        RoomSetParams(
            roomId = "",
            role = "",
            isRoomOwner = false
        )
    )

    fun deleteRoom() {
        roomRepository.deleteRoom(roomExtra.id)
    }
}