package com.alurwa.berkelas.ui.attendancedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.berkelas.model.AttendanceDetailItem
import com.alurwa.berkelas.model.AttendanceDetailUi
import com.alurwa.berkelas.model.AttendanceHeader
import com.alurwa.common.model.Attendance
import com.alurwa.common.model.User
import com.alurwa.common.util.AttendanceType
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

    val userId get() = userRepository.getUserId()

    val observeUsersAndAttendance = userRepository.observeUsersByRoomId()
        .combine(attendanceRepository.observeAttendance(date.toString())) { usersP, attendanceP ->
            Pair(usersP, attendanceP)
        }

    private val _users = MutableStateFlow<List<User>?>(null)
    val users = _users.asStateFlow()

    private val _attendance = MutableStateFlow<Attendance?>(null)
    val attendance = _attendance.asStateFlow()

    private val _attendanceDetailHeader = MutableStateFlow<AttendanceHeader?>(null)
    val attendanceDetailHeader = _attendanceDetailHeader.asStateFlow()

    val attendanceDetailItems =
        users.filterNotNull().combine(attendance.filterNotNull()) { usersP, attendanceP ->
            var presence = 0
            var permit = 0
            var sick = 0
            var uncheck = 0

            val items = List(usersP.size) { index ->
                val user = usersP[index]

                val checkCode2 = let {
                    attendanceP.presense.forEach {
                        if (it == user.uid) {
                            presence += 1
                            return@let AttendanceType.PRESENCE.code
                        }
                    }

                    attendanceP.permit.forEach {
                        if (it == user.uid) {
                            permit += 1
                            return@let AttendanceType.PERMIT.code
                        }
                    }

                    attendanceP.sick.forEach {
                        if (it == user.uid) {
                            sick += 1
                            return@let AttendanceType.SICK.code
                        }
                    }
                    uncheck += 1
                    ""
                }
                val checkCode = getCheckCode(
                    user.uid,
                    attendanceP.presense,
                    attendanceP.permit,
                    attendanceP.sick
                )

                AttendanceDetailItem(userId = user.uid, name = user.fullName, check = checkCode2)
            }

            AttendanceDetailUi(
                info = AttendanceHeader(
                    presence = presence,
                    permit = permit,
                    sick = sick,
                    uncheck = uncheck
                ), items = items
            )
        }

    private fun getCheckCode(
        userId: String,
        presence: List<String>, permit: List<String>, sick: List<String>
    ): String {
        presence.forEach {
            if (it == userId) {
                return AttendanceType.PRESENCE.code
            }
        }

        permit.forEach {
            if (it == userId) {
                return AttendanceType.PERMIT.code
            }
        }

        sick.forEach {
            if (it == userId) {
                return AttendanceType.SICK.code
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

    fun setAttendanceDetailHeader(value: AttendanceHeader) {
        _attendanceDetailHeader.value = value
    }

    fun setAttendance(type: String, userId: String) = attendanceRepository.addAttendanceItem(
        attendanceId = date.toString(),
        type = type,
        userId = userId
    )
}