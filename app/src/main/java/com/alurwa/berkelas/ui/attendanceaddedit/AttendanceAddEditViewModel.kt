package com.alurwa.berkelas.ui.attendanceaddedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.berkelas.extension.EXTRA_MODE
import com.alurwa.common.model.Attendance
import com.alurwa.data.repository.attendance.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AttendanceAddEditViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    val mode = stateHandle.get<Int>(EXTRA_MODE)!!
    val dateExtra = stateHandle.get<Long?>(AttendanceAddEditActivity.EXTRA_DATE)

    private val _date = MutableStateFlow(dateExtra ?: Date().time)
    val date = _date.asStateFlow()

    fun addAttendance(attendance: Attendance) =
        attendanceRepository.addAttendance(attendance)
}