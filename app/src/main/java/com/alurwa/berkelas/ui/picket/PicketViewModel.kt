package com.alurwa.berkelas.ui.picket

import androidx.lifecycle.ViewModel
import com.alurwa.berkelas.model.PicketDayUi
import com.alurwa.berkelas.model.toPicketDayUi
import com.alurwa.common.model.PicketDay
import com.alurwa.common.model.User
import com.alurwa.data.repository.picket.PicketRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class PicketViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val picketRepository: PicketRepository
) : ViewModel() {

    val observePickets = picketRepository.observePickets()

    val observeUsers = userRepository.getUsers()

    private val _picketDays = MutableStateFlow<List<PicketDay>?>(null)
    val picketDays = _picketDays.asStateFlow()

    private val _users = MutableStateFlow<List<User>?>(null)
    val users = _users.asStateFlow()

    val picketDayListUi =
        picketDays.filterNotNull().combine(users.filterNotNull()) { picketDaysP, usersP ->
            List(picketDaysP.size) { index ->
                val picketDay = picketDaysP[index]

                picketDay.toPicketDayUi { map ->
                    usersP.find { it.uid == map }?.fullName.orEmpty()
                }
            }
        }

    fun setUsers(users: List<User>) {
        _users.value = users
    }

    fun setPicketDays(picketDays: List<PicketDay>) {
        _picketDays.value = picketDays
    }

    fun deletePicket(day: Int, picketId: String) = picketRepository.deletePicket(day, picketId)

    fun transformToUntilAWeek(items: List<PicketDayUi>) =
        List(7) { index ->
            val item = items.find { it.day == index }
            item ?: PicketDayUi(day = index, picketsUi = emptyList())
        }

}