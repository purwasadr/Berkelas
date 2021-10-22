package com.alurwa.berkelas.model

data class AttendanceItem(
    val date: Long,
    val presence: Int,
    val permission: Int,
    val sick: Int,
    val notFilled: Int
)
