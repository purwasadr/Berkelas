package com.alurwa.data.repository.subject

import com.alurwa.common.model.Result
import com.alurwa.common.model.Subject
import com.alurwa.common.model.SubjectItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeSubject() = callbackFlow<Result<List<Subject>>> {
        trySend(Result.Loading)

        val listSubject = listOf(
            Subject(
                day = 0,
                subjectItem =  listOf(
                    SubjectItem(
                        subject = "Matematika", startTime = "09:20",
                        endTime = "10:20",
                        teacher = "Dramaturgy"

                    )
                ),
            ),
            Subject(
                day = 1,
                subjectItem =  listOf(
                    SubjectItem(
                        subject = "Geografi",
                        startTime = "11:20",
                        endTime = "12:20",
                        teacher = "Episode"
                    )
                ),
            )
        )

        trySend(Result.Success(listSubject))
        awaitClose {

        }
    }
}