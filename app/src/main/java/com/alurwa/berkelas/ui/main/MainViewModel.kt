package com.alurwa.berkelas.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alurwa.data.repository.auth.AuthRepository
import com.alurwa.data.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val isLogged = authRepository.isLogged()

    val observeUser = userRepository
        .observeUser()
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    fun signOut() {
        authRepository.signOut()
    }

}