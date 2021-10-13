package com.alurwa.data.util

import android.content.SharedPreferences
import androidx.core.content.edit
import com.alurwa.common.di.SessionPreferences
import com.alurwa.common.model.UserRoom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @SessionPreferences
    private val sharedPreferences: SharedPreferences
) {

    fun saveSession(roomId: String, role: String, isRoomOwner: Boolean) {
        sharedPreferences.edit(true) {
            putString(ROOM_ID, roomId)
            putString(ROLE, role)
            putBoolean(IS_ROOM_OWNER, isRoomOwner)
        }
    }

    fun clearSession() {
        sharedPreferences.edit(true) {
            putString(ROOM_ID, "")
        }
    }

    fun getRoomIdNotEmptyOrThrow(): String {
        val roomId = sharedPreferences.getString(ROOM_ID, "") ?: ""

        if (roomId.isEmpty()) {
            throw IllegalStateException("id is empty")
        }

        return roomId
    }

    fun getRoomId(): String {
        return sharedPreferences.getString(ROOM_ID, "") ?: ""
    }

    fun getRole(): String {
        return sharedPreferences.getString(ROOM_ID, "") ?: ""
    }

    fun getUserRoom() = UserRoom(
        roomId = sharedPreferences.getString(ROOM_ID, "") ?: "",
        role = sharedPreferences.getString(ROLE, "") ?: "",
        isRoomOwner = sharedPreferences.getBoolean(IS_ROOM_OWNER, false)

    )

    companion object {
        const val ROOM_ID = "room_id"
        const val ROLE = "role"
        const val IS_ROOM_OWNER = "is_room_owner"
    }
}