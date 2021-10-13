package com.alurwa.berkelas.ui.attendancedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.berkelas.model.AttendanceDetailItem
import com.alurwa.berkelas.util.AttendanceCheck
import com.alurwa.common.model.Attendance
import com.alurwa.common.model.User
import com.alurwa.data.repository.attendance.AttendanceRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class AttendanceDetailViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val userRepository: UserRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val date = stateHandle.get<Long>(AttendanceDetailActivity.EXTRA_DATE)!!

    val observeUsersAndAttendance = userRepository.observeUsersByRoomId()
        .combine(attendanceRepository.observeAttendance(date.toString())) { usersP, attendanceP ->
            Pair(usersP, attendanceP)
        }

    private val _users = MutableStateFlow<List<User>?>(null)
    val users = _users.asStateFlow()

    private val _attendance = MutableStateFlow<Attendance?>(null)
    val attendance = _attendance.asStateFlow()

    val attendanceDetailItems =
        users.filterNotNull().combine(attendance.filterNotNull()) { usersP, attendanceP ->
            List(usersP.size) { index ->
                val user = usersP[index]
                val checkCode = getCheckCode(user.uid, attendanceP.presense, attendanceP.permission, attendanceP.sick)

                AttendanceDetailItem(userId = user.uid, name = user.fullName, check = checkCode)
            }
        }

    private fun getCheckCode(
        userId: String,
        presence: List<String>, permit: List<String>, sick: List<String>
    ): String {
        presence.forEach {
            if (it == userId) {
                return AttendanceCheck.PRESENCE.code
            }
        }

        permit.forEach {
            if (it == userId) {
                return AttendanceCheck.PERMIT.code
            }
        }

        sick.forEach {
            if (it == userId) {
                return AttendanceCheck.SICK.code
            }
        }
        return ""
    }

    fun setAttendance(item: Attendance) {
        _attendance.value = item
    }

    fun setUsers(items: List<User>) {
        _users.value = items
    }
}