package com.alurwa.common.model

data class SignUpParams(
    val email: String,
    val password: String,
    val username: String,
    val fullName: String,
    val nickname: String,
    val dateOfBirth: String,
    val gender: Int,
)
