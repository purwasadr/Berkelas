package com.alurwa.data.repository.auth

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @DispatcherIO
    private val dispatcherIO: CoroutineDispatcher
) {

    fun loginWithEmail(
        email: String,
        password: String
    ): Flow<Result<FirebaseUser>> = flow {
        emit(Result.Loading)

        auth.signInWithEmailAndPassword(
            email,
            password
        ).await().also {
            val user = it.user
                if (user != null) emit(Result.Success(user))
                else throw Exception("Login Failure")
            }

    }.catchToResult().flowOn(dispatcherIO)

    fun signUpWithEmail(
        email: String,
        password: String
    ) = flow {
        emit(Result.Loading)

        auth.createUserWithEmailAndPassword(
            email,
            password
        ).await().also {
            val user = it.user

            if (user != null) emit(Result.Success(user))
            else throw Exception("Login Failure")
        }

    }.catchToResult().flowOn(dispatcherIO)

    fun isLogged() = auth.currentUser != null

    fun signOut() {
        auth.signOut()
    }
}

