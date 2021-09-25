package com.alurwa.common.extension

import com.alurwa.common.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<Result<T>>.catchToResult() = this.catch {
   emit(Result.Error(Exception(it)))
}