package com.alurwa.data.repository.cash

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Cash
import com.alurwa.common.model.Result
import com.alurwa.common.util.autoId
import com.alurwa.data.firebase.cashDataCol
import com.alurwa.data.firebase.cashDataDoc
import com.alurwa.data.firebase.listener
import com.alurwa.data.model.CashAddParams
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

class CashRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher
) {
    private val myRoomId get() = sessionManager.getRoomIdNotEmptyOrThrow()

    fun addCash(cashAddParams: CashAddParams) = flow {
        emit(Result.Loading)

        val generateId = autoId()

        val cash = Cash(
            cashId = generateId,
            date = cashAddParams.date,
            amount = cashAddParams.amount,
            hasPaid = cashAddParams.hasPaid
        )

        firestore.cashDataDoc(roomId = myRoomId, id = generateId).set(cash).await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(dispatcher)

    fun editCash(cash: Cash) {
        firestore.cashDataDoc(cash.cashId, myRoomId).set(cash)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeCashList() = callbackFlow {
        trySend(Result.Loading)

        val listener = firestore.cashDataCol(myRoomId).listener(Cash::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }
    }.catchToResult()

    fun deleteCash(cashId: String) {
        firestore.cashDataDoc(roomId = myRoomId, id = cashId).delete()
    }
}