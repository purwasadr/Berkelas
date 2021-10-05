package com.alurwa.berkelas.ui.cashdatedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alurwa.berkelas.model.CashDateDetailItem
import com.alurwa.common.model.Cash
import com.alurwa.common.model.User
import com.alurwa.data.repository.cash.CashRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashDateDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val cashRepository: CashRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val cashId = stateHandle.get<String>(CashDateDetailActivity.EXTRA_CASH_ID)!!

    val observeUsers = userRepository.observeUsersByRoomId()
    val observeCash = cashRepository.observeCash(cashId)

    private val _users = MutableStateFlow<List<User>?>(null)
    val users = _users.asStateFlow()

    private val _cash = MutableStateFlow<Cash?>(null)
    val cash = _cash.asStateFlow()

    private val _paidedList = MutableStateFlow<List<CashDateDetailItem>>(emptyList())
    val paidedList =  _paidedList.asStateFlow()

    init {
        viewModelScope.launch {
            users.filterNotNull().combine(cash.filterNotNull()) { usersP, cashP ->
                List(usersP.size) { index ->
                    val user = usersP[index]

                    val isAny = cashP.hasPaid.any {
                        user.uid == it
                    }

                    CashDateDetailItem(uid = user.uid, name = user.fullName, isPaid = isAny)
                }
            }.collectLatest {
                _paidedList.value = it
            }
        }
    }

    fun editCash(cash: Cash) {
        cashRepository.editCash(cash)
    }

    fun setUsers(list: List<User>) {
        _users.value = list
    }

    fun setCash(data: Cash) {
        _cash.value = data
    }
}