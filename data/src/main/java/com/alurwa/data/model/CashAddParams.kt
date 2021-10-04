package com.alurwa.data.model

data class CashAddParams(
    var date: Long? = null,
    var amount: Int = 0,
    var hasPaid: List<String> = emptyList()
)
