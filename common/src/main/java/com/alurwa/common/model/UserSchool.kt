package com.alurwa.common.model

data class UserSchool(
    val schoolId: String = "",
    val kelasId: String = ""
) {
    companion object {
        val EMPTY = UserSchool(schoolId = "", kelasId = "")
    }
}
