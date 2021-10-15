package com.alurwa.berkelas.model

sealed class CardHomeItem() {
    data class Content(
        val id: String,
        val caption: String,
        val backgroundColor: String
    ) : CardHomeItem()
    object Add : CardHomeItem()
}
