package com.alurwa.data.model

data class RoomSetParams(
    val roomId: String = "",
    val role: String = "",
    @field:JvmField
    val isRoomOwner: Boolean = false,
)