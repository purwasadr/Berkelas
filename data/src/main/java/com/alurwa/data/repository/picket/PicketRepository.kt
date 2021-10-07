package com.alurwa.data.repository.picket

import com.alurwa.common.model.PicketDay
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.picketCol
import com.alurwa.data.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class PicketRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) {

    val myRoomId get() = sessionManager.getRoomIdNotEmptyOrThrow()

    fun observePickets() = callbackFlow {
        val listener = firestore.picketCol(roomId = myRoomId).listener(PicketDay::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }
    }
}