package com.alurwa.data.repository.auth

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.model.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @DispatcherIO
    private val dispatcherIO: CoroutineDispatcher
) {

    fun loginWithEmail(
        email: String,
        password: String
    ): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)

        auth.signInWithEmailAndPassword(
            email,
            password
        ).await().also {
                if (it.user != null) emit(Result.Success(true))
                else throw Exception("Login Failure")
            }


//        val processAuth = suspendCoroutine<Result<Boolean>> {
//            auth.signInWithEmailAndPassword(
//                email,
//                password
//            ).addOnCompleteListener { task: Task<AuthResult?> ->
//                if (!task.isSuccessful) {
//                    it.resume(Result.Error(task.exception ?: Exception()))
//                } else {
//                    it.resume(Result.Success(true))
//                }
//            }
//        }

        //  emit(processAuth)
    }.catch {
        emit(Result.Error(Exception(it)))
    }.flowOn(Dispatchers.IO)

    fun signUpWithEmail(
        email: String,
        password: String
    ): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)

//        suspendCoroutine<Result<Boolean>> {
//            auth.createUserWithEmailAndPassword(
//                email,
//                password
//            ).addOnCompleteListener { task: Task<AuthResult?> ->
//                if (!task.isSuccessful) {
//                    it.resume(Result.Error(task.exception ?: Exception()))
//                } else {
//                    it.resume(Result.Success(true))
//                }
//            }
//        }.also {
//            emit(it)
//        }

        auth.createUserWithEmailAndPassword(
            email,
            password
        ).await()

        emit(Result.Success(true))
    }.catch {
        emit(Result.Error(Exception(it)))
    }.flowOn(Dispatchers.IO)

    fun isLogged() = auth.currentUser != null
}

