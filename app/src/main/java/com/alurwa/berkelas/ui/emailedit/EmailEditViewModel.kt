package com.alurwa.berkelas.ui.emailedit

import androidx.lifecycle.ViewModel
import com.alurwa.common.model.Result
import com.alurwa.common.model.onError
import com.alurwa.data.model.EditEmailParams
import com.alurwa.data.repository.auth.AuthRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class EmailEditViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    fun editEmail(editEmailParams: EditEmailParams) = flow {
        emit(Result.Loading)
        authRepository.editEmail(editEmailParams).filterNot {
            it is Result.Loading
        }.first().also { result ->
            result.onError {
                emit(result)
                return@flow
            }
        }

        userRepository.editEmail(editEmailParams.newEmail).filterNot {
            it is Result.Loading
        }.first().also {
            emit(it)
        }

    }
}