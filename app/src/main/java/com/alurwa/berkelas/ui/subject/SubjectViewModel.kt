package com.alurwa.berkelas.ui.subject

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.subject.SubjectRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    val observeSubject = subjectRepository.observeSubject()

    val role = userRepository.getUserRoomLocal()

    fun deleteSubject(day: Int, subjectItemId: String) =
        subjectRepository.deleteSubject(day, subjectItemId)
}