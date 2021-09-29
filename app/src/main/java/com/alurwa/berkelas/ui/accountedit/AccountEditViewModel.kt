package com.alurwa.berkelas.ui.accountedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.model.UserWithoutRoom
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AccountEditViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val userWS = stateHandle.get<UserWithoutRoom>(AccountEditActivity.EXTRA_USER_WITHOUT_SCHOOL)!!

    private val _gender = MutableStateFlow(userWS.gender)
    val gender = _gender.asStateFlow()

    private val _profileImgUrl = MutableStateFlow(userWS.profileImgUrl)
    val profileImgUrl = _profileImgUrl.asStateFlow()

    private val _dateOfBirth = MutableStateFlow(userWS.dateOfBirth)
    val dateOfBirth = _dateOfBirth.asStateFlow()

    private val _email = MutableStateFlow(userWS.email)
    val email = _email.asStateFlow()

    fun setGender(gender: Int) {
        _gender.value = gender
    }

    fun editUserWithoutSchool(userWithoutRoom: UserWithoutRoom) =
        userRepository.editUserWithoutRoom(userWithoutRoom)

    fun setDateOfBirth(value: Long?) {
        _dateOfBirth.value = value
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setProfileImgUrl(url: String) {
        _profileImgUrl.value = url
    }
}