package com.alurwa.common.model

data class PicketDay(
    val day: Int = -1,
    val pickets: List<Picket> = emptyList()
)
