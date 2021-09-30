package com.alurwa.data.repository.cash

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Cash
import com.alurwa.common.model.Result
import com.alurwa.data.firebase.cashDoc
import com.alurwa.data.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
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
    fun addCash(cash: Cash) = flow {
        emit(Result.Loading)

        firestore.cashDoc(cash.cashId).set(cash).await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(dispatcher)
}