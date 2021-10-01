package com.alurwa.common.model

data class Cash(
    var cashId: String = "",
    var date: Long = -1,
    var amount: Int = 0,
    var hasPaid: List<String> = emptyList()
) {
    companion object {
        const val FIELD_CASH_ID = "cashId"
        const val FIELD_DATE = "date"
        const val FIELD_AMOUNT = "amount"
        const val FIELD_HAS_PAID = "hasPaid"
    }
}