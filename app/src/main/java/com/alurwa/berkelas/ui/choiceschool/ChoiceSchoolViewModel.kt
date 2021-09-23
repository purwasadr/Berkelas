package com.alurwa.berkelas.ui.choiceschool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alurwa.common.model.Kelas
import com.alurwa.common.model.School
import com.alurwa.data.repository.school.SchoolRepository
import com.alurwa.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChoiceSchoolViewModel @Inject constructor(
    private val schoolRepository: SchoolRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val observeSchools = schoolRepository
        .observeSchools()

    private val schoolLocal = userRepository.getSchoolLocal()

    // Menggunakan null hanya agar tidak melakukan initial load
    private val _schoolsData = MutableStateFlow<List<School>?>(null)
    val schoolData = _schoolsData.asStateFlow()

    private val _selectedSchool = MutableStateFlow<School?>(null)
    val selectedSchool = _selectedSchool.asStateFlow()

    private val _selectedKelas: MutableStateFlow<Kelas?> = MutableStateFlow(null)
    val selectedKelas = _selectedKelas.asStateFlow()

    init {
        viewModelScope.launch {
            // Use filterNotNull for ignore intial value
            _selectedSchool.value = schoolData.filterNotNull().first().find {
                it.id == schoolLocal.schoolId
            }
        }

        viewModelScope.launch {
            _selectedKelas.value = selectedSchool.first()?.kelasList?.find {
                it.id == schoolLocal.kelasId
            }

            selectedSchool.collectIndexed { index, _ ->
                if (index > 0) {
                    setKelas(null)
                }
            }
        }
    }

    fun setKelas(kelas: Kelas?) {
        _selectedKelas.value = kelas
    }

    fun setSchoolsData(schoolsData: List<School>) {
        _schoolsData.value = schoolsData
    }

    fun setSchool(school: School?) {
        _selectedSchool.value = school
    }

    fun getSchools(): List<School> {
        return emptyList()
    }
}