package com.alurwa.berkelas.ui.homecardaddedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.berkelas.extension.EXTRA_MODE
import com.alurwa.common.model.HomeCard
import com.alurwa.data.model.HomeCardAddParams
import com.alurwa.data.repository.maincard.MainCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeCardAddEditViewModel @Inject constructor(
    private val homeCardRepository: MainCardRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val mode = stateHandle.get<Int>(EXTRA_MODE)!!

    private val _color = MutableStateFlow(0)
    val color = _color.asStateFlow()

    fun addHomeCard(homeCardAddParams: HomeCardAddParams) {
        homeCardRepository.addMainCard(homeCardAddParams)
    }

    fun editHomeCard(homeCard: HomeCard) {
        homeCardRepository.editMainCard(homeCard)
    }

    fun setColor(color: Int) {
        _color.value = color
    }
}