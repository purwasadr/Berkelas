package com.alurwa.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomData(
    val id: String = "",
    val roomName: String = "",
    val kelasName: String = "",
    val schoolName: String = "",
    val creatorId: String = ""
) : Parcelable {
    companion object {
        val EMPTY = RoomData(
            id = "", roomName = "", kelasName = "", schoolName = "", creatorId = ""
        )
    }
}
