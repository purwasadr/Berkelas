package com.alurwa.berkelas.ui.picket

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.picket.PicketRepository
import com.alurwa.data.repository.user.UserRepository
import javax.inject.Inject

class PicketViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val picketRepository: PicketRepository
) : ViewModel() {

    val observePickets = picketRepository.observePickets()
}