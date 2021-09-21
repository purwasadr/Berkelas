package com.alurwa.common.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Result.Success) action(this.data)
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onError(action: (value: Exception) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Result.Error) action(this.exception)
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Result.Loading) action()
    return this
}