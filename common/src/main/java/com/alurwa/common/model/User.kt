package com.alurwa.common.model

data class User(
    val email: String,
    val username: String,
    val fullName: String,
    val nickname: String,
    val dateOfBirth: String,
    val gender: Int,
)
