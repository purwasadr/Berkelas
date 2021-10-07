package com.alurwa.common.model

data class Subject(
    val day: Int? = null,
    val subjectItem: List<SubjectItem> = emptyList()
)