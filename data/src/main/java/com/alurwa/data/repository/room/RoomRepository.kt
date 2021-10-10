package com.alurwa.data.repository.room

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Result
import com.alurwa.common.model.RoomData
import com.alurwa.common.util.autoId
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.roomCol
import com.alurwa.data.firebase.roomDoc
import com.alurwa.data.model.RoomAddParams
import com.alurwa.data.util.SessionManager
import com.google.firebase.auth.FirebaseAuth
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
    private val auth: FirebaseAuth,
    private val sessionManager: SessionManager,
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

    /**
     * observe room this user
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeRoom() = callbackFlow<Result<RoomData?>> {
        trySend(Result.Loading)

        val myRoomId = sessionManager.getRoomIdNotEmptyOrThrow()

        val listener = firestore.roomDoc(myRoomId).listener(RoomData::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }
    }.catchToResult()

    /**
     * observe room other user
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeRoom(roomId: String) = callbackFlow<Result<RoomData?>> {
        trySend(Result.Loading)


        val listener = firestore.roomDoc(roomId).listener(RoomData::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }
    }.catchToResult()

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

    fun addRoomData(roomAddParams: RoomAddParams) = flow<Result<Boolean>> {
        val room = roomAddParams.run {
            RoomData(
                id = autoId(),
                roomName = roomName,
                kelasName = kelasName,
                schoolName = schoolName,
                password = password,
                creatorId = auth.uid!!,
            )
        }

        emit(Result.Loading)

        firestore.runTransaction {
            it.set(firestore.roomDoc(room.id), room)
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

    fun deleteRoom(roomId: String) {
        firestore.roomDoc(roomId).delete()
    }

}