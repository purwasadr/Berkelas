package com.alurwa.data.repository.room

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Result
import com.alurwa.common.model.RoomData
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.roomCol
import com.alurwa.data.firebase.roomDoc
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

class RoomRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeRooms() = callbackFlow<Result<List<RoomData>>> {

        trySend(Result.Loading)

        val listener = firestore.roomCol().listener(RoomData::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }

    }

    fun getRooms() = flow<Result<List<RoomData>>> {

        emit(Result.Loading)

        val result = firestore.roomCol().get().await().toObjects(RoomData::class.java)

        emit(Result.Success(result))
    }.catchToResult().flowOn(dispatcher)

    fun getRoom(id: String) = flow<Result<RoomData?>> {

        emit(Result.Loading)

        val result = firestore.roomDoc(id).get().await().toObject(RoomData::class.java)

        emit(Result.Success(result))
    }.catchToResult().flowOn(dispatcher)

    fun addRoomData(roomData: RoomData) = flow<Result<Boolean>> {
        emit(Result.Loading)

        firestore.runTransaction {
            it.set(firestore.roomDoc(roomData.id), roomData)
        }.await()

        emit(Result.Success(true))


    }.catchToResult().flowOn(dispatcher)

    fun ediRoomData(roomData: RoomData) = flow<Result<Boolean>> {
        emit(Result.Loading)

        firestore.runTransaction {
            it.set(firestore.roomDoc(roomData.id), roomData)
        }.await()

        emit(Result.Success(true))


    }.catchToResult().flowOn(dispatcher)

    fun setRoomPassword(roomId: String, password: String) = flow<Result<Boolean>> {
        emit(Result.Loading)

        firestore.runTransaction {
            it.update(firestore.roomDoc(roomId), RoomData.FIELD_PASSWORD, password)
        }.await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(dispatcher)

}