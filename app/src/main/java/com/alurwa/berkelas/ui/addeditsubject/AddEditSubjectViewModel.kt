package com.alurwa.berkelas.ui.addeditsubject

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.model.SubjectItem
import com.alurwa.data.repository.subject.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class  AddEditSubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val mode = stateHandle.get<Int>(AddEditSubjectActivity.EXTRA_MODE)!!

    val subjectItem = stateHandle.get<SubjectItem?>(AddEditSubjectActivity.EXTRA_SUBJECT)

    private val _day = MutableStateFlow<Int>(
        stateHandle.get<Int>(AddEditSubjectActivity.EXTRA_DAY)!!
    )

    val day = _day.asStateFlow()

    private val _startTime = MutableStateFlow<String>(subjectItem?.startTime
        ?: getNowTime()
    )
    val startTime = _startTime.asStateFlow()

    private val _endTime = MutableStateFlow<String>(subjectItem?.startTime
        ?: getNowTime()
    )
    val endTime = _endTime.asStateFlow()

    fun addSubject(day: Int, subject: SubjectItem) = subjectRepository.addSubject(day, subject)

    fun editSubject(day: Int, subject: SubjectItem) = subjectRepository.editSubject(day, subject)

    fun setDay(day: Int) {
        _day.value = day
    }

    fun setStartTime(time: String) {
        _startTime.value = time
    }

    fun setEndTime(time: String) {
        _endTime.value = time
    }

    fun getNowTime()  = SimpleDateFormat("H:mm", Locale.getDefault()).format(Date()).toString()
}