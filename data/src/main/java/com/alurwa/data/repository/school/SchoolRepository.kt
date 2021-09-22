package com.alurwa.data.repository.school

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.model.Result
import com.alurwa.common.model.School
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.schoolCol
import com.alurwa.data.firebase.schoolDoc
import com.alurwa.data.util.SessionManager
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

@OptIn(ExperimentalCoroutinesApi::class)
class SchoolRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    @DispatcherIO
    private val defaultDispatcher: CoroutineDispatcher
) {


    fun observeSchools() = callbackFlow<Result<List<School>>> {
        trySend(Result.Loading)

        val listener = firestore
            .schoolCol()
            .listener(School::class.java) {
                trySendBlocking(it)
            }

        awaitClose {
            listener.remove()
        }
    }

    fun getMySchool() = flow {
        emit(Result.Loading)

        val school = sessionManager.getUserSchool()

        val result = firestore.schoolDoc(
            school.schoolId
        ).get().await().toObject(School::class.java)

        emit(Result.Success(result))
    }.catch {
        emit(Result.Error(Exception(it)))
    }.flowOn(defaultDispatcher)

}