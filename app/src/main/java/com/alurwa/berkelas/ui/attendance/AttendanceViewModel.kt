package com.alurwa.berkelas.ui.attendance

import androidx.lifecycle.ViewModel
import com.alurwa.berkelas.model.AttendanceItem
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
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val observeUsers = userRepository.observeUsersByRoomId()
    val observeAttendanceList = attendanceRepository.observeAttendanceList()
    val observeAttendanceAndUserList = attendanceRepository
        .observeAttendanceList()
        .combine(userRepository.observeUsersByRoomId()) { p1, p2 ->
            Pair(p1, p2)
        }

    private val _users = MutableStateFlow<List<User>?>(null)
    val users = _users.asStateFlow()

    private val _attendanceList = MutableStateFlow<List<Attendance>?>(null)
    val attendanceList = _attendanceList.asStateFlow()

    val observeAttendanceItems = attendanceList.filterNotNull()
        .combine(users.filterNotNull()) { attendanceListP, usersP ->
            List(attendanceListP.size) { index ->
                val attendance = attendanceListP[index]

                val countPresence = attendance.presense.count { countP ->
                    usersP.any {
                        it.uid == countP
                    }
                }

                val countPermission = attendance.permission.count { countP ->
                    usersP.any {
                        it.uid == countP
                    }
                }

                val countSick = attendance.sick.count { countP ->
                    usersP.any {
                        it.uid == countP
                    }
                }

                val countUncheck = usersP.size - (countPresence + countPermission + countSick)

                AttendanceItem(
                    date = attendance.date.toLong(),
                    presence = countPresence,
                    permission = countPermission,
                    sick = countSick,
                    notFilled = countUncheck
                )
            }
        }

    fun setAttendanceList(items: List<Attendance>) {
        _attendanceList.value = items
    }

    fun setUsers(items: List<User>) {
        _users.value = items
    }
}