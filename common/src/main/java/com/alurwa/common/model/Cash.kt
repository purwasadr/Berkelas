package com.alurwa.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cash(
    var cashId: String = "",
    var date: Long? = null,
    var amount: Int = 0,
    var hasPaid: List<String> = emptyList()
) : Parcelable {
    companion object {
        const val FIELD_CASH_ID = "cashId"
        const val FIELD_DATE = "date"
        const val FIELD_AMOUNT = "amount"
        const val FIELD_HAS_PAID = "hasPaid"
    }
}