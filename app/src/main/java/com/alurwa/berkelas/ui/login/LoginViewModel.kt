package com.alurwa.berkelas.ui.login

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun loginWithEmail(email: String, password: String) =
        authRepository.loginWithEmail(email, password)
}