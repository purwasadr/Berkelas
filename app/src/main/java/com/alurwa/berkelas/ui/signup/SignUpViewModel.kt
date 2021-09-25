package com.alurwa.berkelas.ui.signup

import androidx.lifecycle.ViewModel
import com.alurwa.common.model.Result
import com.alurwa.common.model.SignUpParams
import com.alurwa.common.model.User
import com.alurwa.data.repository.auth.AuthRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {


    fun signUpWithEmail(signUpParams: SignUpParams) = flow<Result<Boolean>> {
        emit(Result.Loading)

        authRepository.signUpWithEmail(signUpParams.email, signUpParams.password)
            .first {
                it !is Result.Loading
            }.also {

                // Jika Result error akan langsung return ke flow
                if (it is Result.Error) {
                    emit(it)
                    return@flow
                }
            }

        val user = User(
            email = signUpParams.email,
            username = signUpParams.username,
            fullName = signUpParams.fullName,
            nickname = signUpParams.nickname,
            dateOfBirth = signUpParams.dateOfBirth,
            gender = signUpParams.gender
        )

        userRepository.addUser(user).first {
            it !is Result.Loading
        }.also {
            emit(it)
        }
    }
}