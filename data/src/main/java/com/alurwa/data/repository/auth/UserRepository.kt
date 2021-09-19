package com.alurwa.data.repository.auth

import com.alurwa.common.model.Result
import com.alurwa.common.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun addUser(user: User) = flow<Result<Boolean>> {
        emit(Result.Loading)

        suspendCoroutine<Result<Boolean>> {
            firestore.collection("user")
                .add(user)
                .addOnSuccessListener { _ ->
                    it.resume(Result.Success(true))
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeUser(id: String) = callbackFlow<Result<User>> {
        trySend(Result.Loading)

        firestore.collection("user").document(id)
            .addSnapshotListener { value, error ->
                val result = value?.toObject(User::class.java)

            }

        awaitClose {

        }
    }
}