package com.alurwa.berkelas.ui.addeditsubject

import androidx.lifecycle.ViewModel
import com.alurwa.data.repository.subject.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditSubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

}