package com.alurwa.berkelas.ui.cashaddedit

import androidx.lifecycle.ViewModel
import com.alurwa.data.model.CashAddParams
import com.alurwa.data.repository.cash.CashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CashAddEditViewModel @Inject constructor(
    private val cashRepository: CashRepository
) : ViewModel() {
    fun addCash(cashAddParams: CashAddParams) = cashRepository.addCash(cashAddParams)
}