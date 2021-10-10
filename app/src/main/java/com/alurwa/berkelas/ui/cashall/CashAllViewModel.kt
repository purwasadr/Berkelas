package com.alurwa.berkelas.ui.cashall

import androidx.lifecycle.ViewModel
import com.alurwa.berkelas.model.CashAllDateItem
import com.alurwa.berkelas.model.CashAllPersonItem
import com.alurwa.berkelas.util.DateTimeUtil
import com.alurwa.common.model.Cash
import com.alurwa.common.model.User
import com.alurwa.data.repository.cash.CashRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import timber.log.Timber
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

    private val _cashList = MutableStateFlow<List<Cash>?>(null)

    val cashList = _cashList.asStateFlow()

    val userCashCombine = users
        .filterNotNull()
        .combine(
            cashList.filterNotNull()
        ) { usersCombine, cashListCombine ->

            // Membuat daftar untuk List Adapter
            List(usersCombine.size) {
                val user = usersCombine[it]

                Timber.d("amount" + cashListCombine.size)

                // Menjumlahkan properti Cash.amount jika userId di temukan di
                // properti Cash.hasPaid
                val amount = cashListCombine.sumOf { cash ->
                    val isPaid = cash.hasPaid.any { hasPaid ->
                        Timber.d("any : %s", hasPaid)
                        hasPaid == user.uid
                    }
                    Timber.d("isPaid : %s", isPaid.toString())
                    if (isPaid) 0 else cash.amount
                }

                Timber.d("amount" + amount.toString())

                CashAllPersonItem(
                    id = user.uid, name = user.fullName, amount = amount
                )
            }
        }

    val cashUserCombine = cashList
        .filterNotNull()
        .combine(
            users.filterNotNull()
        ) { cashListCombine,  usersCombine ->

            // Membuat daftar untuk List Adapter
            List(cashListCombine.size) {
                val cash = cashListCombine[it]

                Timber.d("amount" + cashListCombine.size)

                var count = 0

                // Mengubah data dari properti date ke tanggal string
                val dateString = DateTimeUtil.convertDateMillisToString(cash.date ?: -1)

                // Menjumlahkan nilai dari properti Cash.amount jika didalam properti Cash.isPaid
                // terdapat userId dari user
                val amount = usersCombine.sumOf { user ->
                    val isPaid = cash.hasPaid.any { hasPaid ->
                        Timber.d("any : %s", hasPaid)
                        hasPaid == user.uid
                    }

                    if (isPaid) 0 else {
                        count++
                        cash.amount
                    }
                }

                Timber.d("amount" + amount.toString())

                CashAllDateItem(
                    id = cash.cashId, date = dateString, amount = amount, count = count
                )
            }
        }

    fun deleteCash(cashId: String)  {
        cashRepository.deleteCash(cashId)
    }

    fun setUsers(users: List<User>) {
        _users.value = users
    }

    fun setCashList(cashList: List<Cash>) {
        _cashList.value = cashList
    }
}