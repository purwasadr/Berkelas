package com.alurwa.berkelas.ui.picketaddedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alurwa.berkelas.ui.picketaddedit.PicketAddEditActivity.Companion.MODE_EDIT
import com.alurwa.common.model.User
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
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

    val userId = stateHandle.get<String?>(PicketAddEditActivity.EXTRA_USER_ID).also {
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
                userRepository.getUser(userId!!).collectLatest { result ->
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

    fun getUsers() = userRepository.getUsers()

    fun addPicket() {

    }

    fun setSelectedUser(userId: User) {
        _selectedUser.value = userId
    }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }
}