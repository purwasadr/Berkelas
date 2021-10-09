package com.alurwa.data.repository.picket

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Picket
import com.alurwa.common.model.PicketDay
import com.alurwa.common.model.Result
import com.alurwa.common.util.autoId
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.picketCol
import com.alurwa.data.firebase.picketDoc
import com.alurwa.data.model.PicketAddParams
import com.alurwa.data.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class PicketRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher
) {
    private val myRoomId get() = sessionManager.getRoomIdNotEmptyOrThrow()

    fun addPicket(day: Int, picketAddParams: PicketAddParams) = flow {
        emit(Result.Loading)

        val picket = picketAddParams.toPicketAutoId()

        val ref = firestore.picketDoc(roomId = myRoomId, picketId = day.toString())

        firestore.runTransaction {
            val picketDay = it.get(ref).toObject(PicketDay::class.java)

            val pickets = picketDay?.pickets ?: emptyList()

            it.set(
                ref,
                PicketDay(
                    day = day,
                    pickets = pickets.plus(picket)
                )
            )
        }.await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(dispatcher)

    fun editPicket(day: Int, picket: Picket) = flow {
        emit(Result.Loading)

        val ref = firestore.picketDoc(roomId = myRoomId, picketId = day.toString())

        firestore.runTransaction {
            val picketDay = it.get(ref).toObject(PicketDay::class.java)
            val pickets = picketDay?.pickets!!
            val picketMap = pickets.map { item ->
                if (picket.id == item.id) {
                    picket
                } else {
                    item
                }
            }

            it.set(
                ref,
                PicketDay(
                    day = day, pickets = picketMap
                )
            )
        }.await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(dispatcher)

    fun observePickets() = callbackFlow {
        val listener = firestore.picketCol(roomId = myRoomId).listener(PicketDay::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }
    }

    private fun PicketAddParams.toPicketAutoId() = Picket(
        id = autoId(), userId = userId, note = note
    )

}