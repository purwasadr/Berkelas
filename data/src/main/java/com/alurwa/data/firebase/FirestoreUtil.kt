package com.alurwa.data.firebase

import com.alurwa.common.model.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import timber.log.Timber

inline fun <T> DocumentReference.listener(clazz: Class<T>, crossinline block: (result: Result<T?>) -> Unit) =
    this.addSnapshotListener { value, error ->
        val valueObject = value?.toObject(clazz)
        if (error != null) {
            block(Result.Error(error))

            Timber.e(error,"Listen failed")
            return@addSnapshotListener
        }

        if (valueObject != null && value.exists()) {
            block(Result.Success(valueObject))
        } else {
            block(Result.Success(null))
        }
    }

inline fun <T> CollectionReference.listener(clazz: Class<T>, crossinline block: (result: Result<List<T>>) -> Unit) =
    this.addSnapshotListener { value, error ->
        if (error != null) {
            block(Result.Error(error))

            Timber.e(error,"Listen failed")
            return@addSnapshotListener
        }

        if (value != null) {
            val resultValue = value.documents.mapNotNull { snapshot ->
                snapshot.toObject(clazz)
            }

            block(Result.Success(resultValue))
        }
    }