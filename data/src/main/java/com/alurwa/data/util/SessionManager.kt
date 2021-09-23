package com.alurwa.data.util

import android.content.SharedPreferences
import androidx.core.content.edit
import com.alurwa.common.di.SessionPreferences
import com.alurwa.common.model.UserSchool
import javax.inject.Inject

class SessionManager @Inject constructor(
    @SessionPreferences
    private val sharedPreferences: SharedPreferences
) {

    fun saveUser(schoolId: String, kelasId: String) {
        sharedPreferences.edit(true) {
            putString(SCHOOL_ID, schoolId)
            putString(KELAS_ID, kelasId)
        }
    }

    fun clearUser() {
        sharedPreferences.edit(true) {
            putString(SCHOOL_ID, "")
            putString(KELAS_ID, "")
        }
    }

    fun getUserSchool(): UserSchool {
        val schoolId = sharedPreferences.getString(SCHOOL_ID, "") ?: ""
        val kelasId = sharedPreferences.getString(KELAS_ID, "") ?: ""

        return UserSchool(schoolId = schoolId, kelasId = kelasId)
    }

    companion object {
        const val SCHOOL_ID = "school_id"
        const val KELAS_ID = "kelas_id"
    }
}