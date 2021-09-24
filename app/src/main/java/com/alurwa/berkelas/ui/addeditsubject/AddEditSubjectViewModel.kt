package com.alurwa.berkelas.ui.addeditsubject

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alurwa.common.model.SubjectItem
import com.alurwa.data.repository.subject.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditSubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    val mode = stateHandle.get<Int>(AddEditSubjectActivity.EXTRA_MODE)!!
    val subjectItem = stateHandle.get<SubjectItem?>("Ss")
    val day = stateHandle.get<Int>(AddEditSubjectActivity.EXTRA_DAY)!!


    fun addSubject(day: Int, subject: SubjectItem) = subjectRepository.addSubject(day, subject)

    fun editSubject(day: Int, subject: SubjectItem) = subjectRepository.editSubject(day, subject)
}