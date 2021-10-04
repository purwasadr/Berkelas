package com.alurwa.berkelas.ui.roomdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Result
import com.alurwa.common.model.RoomData
import com.alurwa.data.repository.room.RoomRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val room = stateHandle.get<RoomData>(RoomDetailActivity.EXTRA_ROOM)!!

    val isChoice = stateHandle.get<Boolean>(RoomDetailActivity.EXTRA_IS_CHOICE)!!

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

    fun applyRoom2() = flow<Result<Boolean>> {

        userRepository.editRoomId(room.id).first {
            it !is Result.Loading
        }.also {
            emit(it)
        }
    }.catchToResult()
}