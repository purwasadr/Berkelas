package com.alurwa.data.repository.subject

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Result
import com.alurwa.common.model.Subject
import com.alurwa.common.model.SubjectItem
import com.alurwa.data.firebase.listener
import com.alurwa.data.firebase.subjectSharedCol
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

class SubjectRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeSubject() = callbackFlow<Result<List<Subject>>> {
        trySend(Result.Loading)

        val roomId = sessionManager.getMyRoom()

        val listener = firestore.subjectSharedCol(roomId)
            .listener(Subject::class.java) {
                trySendBlocking(it)
            }

        awaitClose {
            listener.remove()
        }
    }.catchToResult()

    fun addSubject(day: Int, subjectItem: SubjectItem) = flow {
        emit(Result.Loading)

        val roomId = sessionManager.getMyRoom()
        val ref = firestore.subjectSharedCol(roomId)
            .document(day.toString())

        firestore.runTransaction {
            val subjectResult = it.get(ref)
                .toObject(Subject::class.java)

            val subjectItemResult = subjectResult?.subjectItem ?: emptyList()

            it.set(
                ref,
                Subject(
                    day = day, subjectItem = subjectItemResult.plus(subjectItem)
                )
            )
        }.await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(dispatcher)

    fun editSubject(day: Int, subjectItem: SubjectItem) = flow {
        emit(Result.Loading)

        val roomId = sessionManager.getMyRoom()

        val ref = firestore.subjectSharedCol(roomId)
            .document(day.toString())

        firestore.runTransaction {
            val subjectResult = it.get(ref)
                .toObject(Subject::class.java)

            val subjectItemResult = subjectResult?.subjectItem!!

            it.set(
                ref,
                Subject(
                    day = day, subjectItem = subjectItemResult.map { item ->
                        if (subjectItem.id == item.id) {
                            subjectItem
                        } else {
                            item
                        }
                    }
                )
            )
        }.await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(dispatcher)
}