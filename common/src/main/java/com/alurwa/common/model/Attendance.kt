package com.alurwa.common.model

data class Attendance(
    val date: String = "",
    val presense: List<String> = emptyList(),
    val permission: List<String> = emptyList(),
    val sick: List<String> = emptyList()
)
