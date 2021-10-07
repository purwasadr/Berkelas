package com.alurwa.common.model

data class User(
    val uid: String = "",
    val email: String = "",
    val profileImgUrl: String = "",
    val username: String = "",
    val fullName: String = "",
    val nickname: String = "",
    val roomId: String = "",
    val dateOfBirth: Long? = null,
    val gender: Int = 0,
    val role: String = ""
) {
    companion object {
        val EMPTY = User(
            uid = "",
            email = "",
            profileImgUrl = "",
            username = "",
            fullName = "",
            nickname = "",
            roomId = "",
            dateOfBirth = null,
            gender = 0
        )

        const val FIELD_ROOM_ID = "roomId"
        const val FIELD_PROFILE_IMG_URL = "profileImgUrl"
        const val FIELD_ROLE = "role"
    }
}
