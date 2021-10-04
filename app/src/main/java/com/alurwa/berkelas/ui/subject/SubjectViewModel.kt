package com.alurwa.berkelas.ui.subject

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.subject.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
) : ViewModel() {
    val observeSubject = subjectRepository.observeSubject()

    fun deleteSubject(day: Int, subjectItemId: String) =
        subjectRepository.deleteSubject(day, subjectItemId)

}