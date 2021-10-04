package com.alurwa.data.util

import android.content.SharedPreferences
import androidx.core.content.edit
import com.alurwa.common.di.SessionPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(
    @SessionPreferences
    private val sharedPreferences: SharedPreferences
) {

    fun saveUser(roomId: String) {
        sharedPreferences.edit(true) {
            putString(ROOM_ID, roomId)
        }
    }

    fun clearUser() {
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

    fun getUserRoomNotEmptyOrThrow(): String {
        val roomId = sharedPreferences.getString(ROOM_ID, "") ?: ""

        if (roomId.isEmpty()) {
            throw IllegalStateException("id is empty")
        }

        return roomId
    }

    companion object {
        const val ROOM_ID = "room_id"
    }
}