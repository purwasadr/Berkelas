package com.alurwa.berkelas.ui.roomdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.model.Result
import com.alurwa.common.model.RoomData
import com.alurwa.common.model.User
import com.alurwa.data.repository.room.RoomRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val room = stateHandle.get<RoomData>(RoomDetailActivity.EXTRA_ROOM)!!

    val userOther = userRepository.getUser(room.creatorId)

    private val _creatorName = MutableStateFlow<String>("")
    val creatorName = _creatorName.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    val observeUser = userRepository.observeUser()

    fun setCreatorName(name: String) {
        _creatorName.value = name
    }

    fun setUser(user: User) {
        _user.value = user
    }

    fun applyRoom(password: String) = flow<Result<Boolean>> {
        val roomId = userRepository.getRoomIdLocal()

        roomRepository.setRoomPassword(roomId, password)
            .first {
                it !is Result.Loading
            }.also {
                // Jika Result error akan langsung return ke flow
                if (it is Result.Error) {
                    emit(it)
                    return@flow
                }
            }

        userRepository.editRoomId(roomId).first {
            it !is Result.Loading
        }.also {
            emit(it)
        }
    }

    fun applyRoom2() = userRepository.editRoomId(room.id)
    fun removeRoom() = userRepository.editRoomId("")
}