package com.alurwa.data.repository.maincard

import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.HomeCard
import com.alurwa.common.model.Result
import com.alurwa.common.util.autoId
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.mainCardCol
import com.alurwa.data.firebase.mainCardDoc
import com.alurwa.data.model.HomeCardAddParams
import com.alurwa.data.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class MainCardRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager

) {
    val myRoomId get() = sessionManager.getRoomIdNotEmptyOrThrow()
    private val myRoomIdNotNull = sessionManager.getRoomId()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeMainCardList() = callbackFlow {
        trySend(Result.Loading)

        val listener = firestore.mainCardCol(roomId = myRoomId).listener(HomeCard::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }
    }.catchToResult()

    fun addMainCard(homeCardAddParams: HomeCardAddParams) {
        val mainCard = HomeCard(id = autoId(),
            text = homeCardAddParams.text,
            backgroundColor = homeCardAddParams.backgroundColor
        )

        if (myRoomIdNotNull.isNotEmpty()) {
            firestore.mainCardDoc(roomId = myRoomId, mainCardId = mainCard.id).set(mainCard)
        }
    }

    fun editMainCard(homeCard: HomeCard) {
        if (myRoomIdNotNull.isNotEmpty()) {
            firestore.mainCardDoc(roomId = myRoomId, mainCardId = homeCard.id).set(homeCard)
        }
    }

    fun deleteMainCard(id: String) {
        if (myRoomIdNotNull.isNotEmpty()) {
            firestore.mainCardDoc(roomId = myRoomId, mainCardId = id).delete()
        }
    }
}