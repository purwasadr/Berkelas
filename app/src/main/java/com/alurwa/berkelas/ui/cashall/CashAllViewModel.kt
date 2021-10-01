package com.alurwa.berkelas.ui.cashall

import androidx.lifecycle.ViewModel
import com.alurwa.common.model.Cash
import com.alurwa.common.model.User
import com.alurwa.data.repository.cash.CashRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CashAllViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val cashRepository: CashRepository
) : ViewModel() {
    val observeUserByRoomId = userRepository.observeUsersByRoomId()

    val cash = cashRepository.observeCashList()

    private val _users = MutableStateFlow<List<User>?>(null)

    val users = _users.asStateFlow()

    private val _cash = MutableStateFlow<List<Cash>?>(null)

    val cashList = _cash.asStateFlow()
}