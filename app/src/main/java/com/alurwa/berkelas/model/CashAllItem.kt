package com.alurwa.berkelas.model

sealed class CashAllItem {
    data class Cash(
        val id: String,
        val date: String,
        val amount: Int,
        val count: Int,
    ) : CashAllItem()

    data class People(
        val id: String,
        val name: String,
        val amount: Int,
    ) : CashAllItem()
}