package com.alurwa.berkelas.ui.picketaddedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alurwa.berkelas.model.PicketUi
import com.alurwa.berkelas.ui.picketaddedit.PicketAddEditActivity.Companion.MODE_EDIT
import com.alurwa.common.model.*
import com.alurwa.data.model.PicketAddParams
import com.alurwa.data.repository.picket.PicketRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicketAddEditViewModel @Inject constructor(
    private val picketRepository: PicketRepository,
    private val userRepository: UserRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val mode = stateHandle.get<Int>(PicketAddEditActivity.EXTRA_MODE)!!.also {
        if (it !in 0..1 ) {
            throw IllegalStateException("Unknown Mode")
        }
    }

    val dayExtra = stateHandle.get<Int>(PicketAddEditActivity.EXTRA_DAY) ?: 0

    private val _day = MutableStateFlow<Int>(dayExtra)
    val day = _day.asStateFlow()

    val picket = stateHandle.get<PicketUi?>(PicketAddEditActivity.EXTRA_PICKET).also {
        if (mode == MODE_EDIT) {
            if (it == null) {
                throw Exception("In mode edit this cannot null")
            }
        }
    }

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        if (mode == MODE_EDIT) {
            viewModelScope.launch {
                userRepository.getUser(picket?.userId!!).collectLatest { result ->
                    result.onSuccess {
                        _selectedUser.value = it
                        setIsLoading(false)
                    }.onLoading {
                        setIsLoading(true)
                    }.onError {
                        setIsLoading(false)
                    }
                }
            }
        }
    }

    fun getUsersByRoomId() = userRepository.getUsersByRoomId()

    fun addPicket(picketAddParams: PicketAddParams) = picketRepository.addPicket(
        day.value,
        picketAddParams
    )

    fun editPicket(picket: Picket) = picketRepository.editPicket(day.value, picket)

    fun setSelectedUser(userId: User) {
        _selectedUser.value = userId
    }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun setDay(day: Int) {
        _day.value = day
    }
}