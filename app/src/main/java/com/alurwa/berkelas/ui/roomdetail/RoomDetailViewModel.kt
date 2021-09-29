package com.alurwa.berkelas.ui.roomdetail

import androidx.lifecycle.ViewModel
import com.alurwa.common.model.Result
import com.alurwa.data.repository.room.RoomRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun applyRoom(roomId: String, password: String) = flow<Result<Boolean>> {
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
}