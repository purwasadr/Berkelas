package com.alurwa.berkelas.ui.roomuserdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.model.User
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoomUserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val userId = stateHandle.get<String>(RoomUserDetailActivity.EXTRA_USER_ID)!!

    val observeUser = userRepository.observeUser(userId)

    private val _user = MutableStateFlow(User.EMPTY)
    val user = _user.asStateFlow()

    fun setUser(userParam: User) {
        _user.value = userParam
    }

    fun editRole(role: String) {
        userRepository.editRole(userId, role)
    }
}