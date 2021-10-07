package com.alurwa.data.repository.user

import android.net.Uri
import com.alurwa.common.di.ApplicationScope
import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Result
import com.alurwa.common.model.User
import com.alurwa.common.model.UserWithoutRoom
import com.alurwa.common.model.onSuccess
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.userCol
import com.alurwa.data.firebase.userDoc
import com.alurwa.data.util.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    @DispatcherIO
    private val defaultDispatcher: CoroutineDispatcher,
    private val auth: FirebaseAuth,
    private val sessionManager: SessionManager,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) {

    fun addUser(user: User) = flow {
        emit(Result.Loading)
        firestore.userCol()
            .document(user.uid)
            .set(user)
            .await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(defaultDispatcher)

    fun getUser(uid: String) = flow {
        emit(Result.Loading)

        firestore.userCol()
            .document(uid).get().await().toObject(User::class.java)
            .also {
                emit(Result.Success(it))
            }

    }.catchToResult().flowOn(defaultDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeUser() = callbackFlow<Result<User?>> {
        trySend(Result.Loading)

        val ree = firestore
            .userDoc(auth.uid!!)
            .listener(User::class.java) {
                trySendBlocking(it)
                it.onSuccess { user ->
                    sessionManager.saveUser(
                        user?.roomId ?: ""
                    )
                }
            }

        awaitClose {
            ree.remove()
        }
    }.catchToResult()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeUsersByRoomId() = callbackFlow<Result<List<User>>> {
        trySend(Result.Loading)

        val ree = firestore
            .userCol()
            .whereEqualTo(User.FIELD_ROOM_ID, sessionManager.getRoomIdNotEmptyOrThrow())
            .listener(User::class.java) {
                trySendBlocking(it)
            }

        awaitClose {
            ree.remove()
        }
    }.catchToResult()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeUser(id: String) = callbackFlow<Result<User?>> {
        trySend(Result.Loading)

        val ree = firestore
            .userCol()
            .document(id)
            .listener(User::class.java) {
                trySendBlocking(it)
            }

        awaitClose {
            ree.remove()
        }
    }.catchToResult()

    fun getRoomIdLocal() = sessionManager.getRoomId()

    fun getRoomIdLocalNotEmptyOrThrow() = sessionManager.getRoomIdNotEmptyOrThrow()

    fun editUserWithoutRoom(userWithoutRoom: UserWithoutRoom) = flow {
        emit(Result.Loading)

        firestore.userDoc(userWithoutRoom.uid)

        firestore.runTransaction {
            it.set(firestore.userDoc(userWithoutRoom.uid), userWithoutRoom, SetOptions.merge())
        }.await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(defaultDispatcher)

    fun editRoomId(roomId: String) = flow {
        emit(Result.Loading)

        val ref = firestore.userDoc(auth.uid!!)

        firestore.runTransaction {
            it.update(ref, User.FIELD_ROOM_ID, roomId)
        }.await()

        sessionManager.saveUser(
            roomId
        )

        emit(Result.Success(true))
    }.catchToResult().flowOn(defaultDispatcher)

    fun changeImgProfile(uri: Uri) = flow<Result<Uri>> {
        emit(Result.Loading)

        val ww = applicationScope.async(defaultDispatcher) {
            try {
                val uid = auth.uid!!
                val profileImageRef =  storage.getReference("profile_image/$uid.jpg")

                profileImageRef.putFile(uri).await()
                profileImageRef.downloadUrl.await().let {
                    firestore.userDoc(uid).update(User.FIELD_PROFILE_IMG_URL, it.toString())
                    Result.Success(it)
                }

            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }

        emit(ww.await())
    }

    fun editRole(uid: String, role: String) {
        firestore.userDoc(uid).update(User.FIELD_ROLE, role)
    }
}