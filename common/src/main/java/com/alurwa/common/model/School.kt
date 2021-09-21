package com.alurwa.common.model

data class School(
    val schoolId: String = "",
    val kelasId: String = ""
) {
    companion object {
        val EMPTY = School(schoolId = "", kelasId = "")
    }
}
