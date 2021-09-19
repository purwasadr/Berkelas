package com.alurwa.berkelas.ui.main

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val isLogged = authRepository.isLogged()
    fun signOut() {
        authRepository.signOut()
    }
}