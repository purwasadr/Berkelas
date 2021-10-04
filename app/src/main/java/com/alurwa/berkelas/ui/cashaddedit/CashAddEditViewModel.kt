package com.alurwa.berkelas.ui.cashaddedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.model.Cash
import com.alurwa.data.model.CashAddParams
import com.alurwa.data.repository.cash.CashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CashAddEditViewModel @Inject constructor(
    private val cashRepository: CashRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val mode = stateHandle.get<Int>(CashAddEditActivity.EXTRA_MODE)!!
    val cash = stateHandle.get<Cash?>(CashAddEditActivity.EXTRA_CASH)

    private val _date = MutableStateFlow<Long?>(cash?.date ?: Date().time)
    val date = _date.asStateFlow()

    fun addCash(cashAddParams: CashAddParams) = cashRepository.addCash(cashAddParams)

    fun editCash(cash: Cash) {
        cashRepository.editCash(cash)
    }

    fun setDate(date: Long?) {
        _date.value = date
    }
}