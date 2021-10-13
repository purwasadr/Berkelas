package com.alurwa.data.repository.attendance

import com.alurwa.common.di.DispatcherIO
import com.alurwa.common.extension.catchToResult
import com.alurwa.common.model.Attendance
import com.alurwa.common.model.Result
import com.alurwa.data.firebase.attendanceCol
import com.alurwa.data.firebase.attendanceDoc
import com.alurwa.data.firebase.listener
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

@OptIn(ExperimentalCoroutinesApi::class)
class AttendanceRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher
) {

    private val myRoomId get() = sessionManager.getRoomIdNotEmptyOrThrow()

    fun addAttendance(attendance: Attendance) = flow {
        emit(Result.Loading)

        val ref = firestore.attendanceDoc(roomId = myRoomId, attendanceId = attendance.date)

        firestore.runTransaction {
            it.get(ref).exists().also { p ->
                if (p) throw Exception("Udah ada")
            }

            it.set(ref, attendance)
        }.await()

        emit(Result.Success(true))
    }.catchToResult().flowOn(dispatcher)


    fun observeAttendanceList() = callbackFlow {
        trySend(Result.Loading)

        val listener = firestore.attendanceCol(myRoomId).listener(Attendance::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }
    }

    fun observeAttendance(date: String) = callbackFlow {
        trySend(Result.Loading)

        val listener = firestore.attendanceDoc(myRoomId, date).listener(Attendance::class.java) {
            trySendBlocking(it)
        }

        awaitClose {
            listener.remove()
        }
    }
}