package com.alurwa.berkelas.model

data class AttendanceHeader(
    val presence: Int,
    val permit: Int,
    val sick: Int,
    val uncheck: Int
)
