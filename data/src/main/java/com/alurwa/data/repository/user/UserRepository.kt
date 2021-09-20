package com.alurwa.data.repository.user

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.model.Result
import com.alurwa.common.model.User
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.userCol
import com.alurwa.data.firebase.userDoc
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @DispatcherIO
    private val defaultDispatcher: CoroutineDispatcher,
    private val auth: FirebaseAuth,
) {

    fun addUser(user: User) = flow {
        emit(Result.Loading)
        firestore.userCol()
            .add(user)
            .await()

        emit(Result.Success(true))
    }.catch {
        emit(Result.Error(Exception(it)))
    }.flowOn(defaultDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeUser() = callbackFlow<Result<User?>> {
        trySend(Result.Loading)

       // val uid = auth.uid

        val ree = firestore
            .userDoc(auth.uid!!)
            .listener(User::class.java) {
                trySendBlocking(it)
            }

        awaitClose {
            ree.remove()
        }
    }.catch {
        emit(Result.Error(Exception(it)))
    }.flowOn(defaultDispatcher)


}